package ssdbrestframework;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import ssdbrestframework.annotations.ApiResponseExample;
import ssdbrestframework.annotations.Endpoint;
import ssdbrestframework.annotations.RequestParam;
import ssdbrestframework.annotations.RestController;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

@Log
public class OpenApiGenerator {

    private OpenApiGenerator() {
    }

    public static final String COMPONENTS_SCHEMAS = "#/components/schemas/";
    public static final String ARRAY = "array";
    public static final String OBJECT = "object";
    public static final String SCHEMAS = "schemas";

    public static ObjectNode generate(String basePackage) {
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode openApi = objectMapper.createObjectNode();
        openApi.put("openapi", "3.0.0");

        ObjectNode info = objectMapper.createObjectNode();
        info.put("title", "SSDB API");
        info.put("version", "1.0.0");
        openApi.set("info", info);

        ObjectNode paths = objectMapper.createObjectNode();
        ObjectNode components = objectMapper.createObjectNode().set(SCHEMAS, objectMapper.createObjectNode());

        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(RestController.class);

        for (Class<?> controller : controllers) {
            RestController restController = controller.getAnnotation(RestController.class);
            String basePath = restController.path();

            for (var method : controller.getMethods()) {
                if (method.isAnnotationPresent(Endpoint.class)) {
                    Endpoint endpoint = method.getAnnotation(Endpoint.class);
                    String fullPath = basePath + endpoint.path();

                    ObjectNode pathItem = paths.has(fullPath) ? (ObjectNode) paths.get(fullPath) : objectMapper.createObjectNode();

                    ObjectNode operation = objectMapper.createObjectNode();
                    operation.put("summary", method.getName());
                    operation.put("operationId", method.getName());

                    ArrayNode parameters = objectMapper.createArrayNode();
                    for (var param : method.getParameters()) {
                        if (param.isAnnotationPresent(RequestParam.class)) {
                            ObjectNode paramSpec = objectMapper.createObjectNode();
                            paramSpec.set("name", TextNode.valueOf(param.getAnnotation(RequestParam.class).value()));
                            paramSpec.set("in", TextNode.valueOf("query"));
                            paramSpec.set("required", BooleanNode.valueOf(true));
                            ObjectNode schema = objectMapper.createObjectNode();
                            schema.set("type", TextNode.valueOf("string"));
                            paramSpec.set("schema", schema);
                            parameters.add(paramSpec);
                        }
                    }
                    operation.set("parameters", parameters);

                    if (method.isAnnotationPresent(ApiResponseExample.class)) {
                        ApiResponseExample responseModel = method.getAnnotation(ApiResponseExample.class);
                        Class<?> modelClass = responseModel.value();
                        boolean isArray = responseModel.isArray();

                        addSchemaForClass(components, modelClass, objectMapper, isArray);

                        ObjectNode responses = objectMapper.createObjectNode();
                        ObjectNode successResponse = objectMapper.createObjectNode();
                        successResponse.put("description", "Successful response");
                        ObjectNode content = objectMapper.createObjectNode();
                        ObjectNode jsonSchema = objectMapper.createObjectNode();
                        jsonSchema.put("$ref", COMPONENTS_SCHEMAS + (isArray ? modelClass.getSimpleName() + "List" : modelClass.getSimpleName()));
                        content.set("application/json", objectMapper.createObjectNode().set("schema", jsonSchema));

                        successResponse.set("content", content);
                        responses.set("200", successResponse);
                        operation.set("responses", responses);
                    }

                    pathItem.set(endpoint.method().toString().toLowerCase(), operation);

                    paths.set(fullPath, pathItem);
                }
            }
        }

        openApi.set("paths", paths);
        openApi.set("components", components);

        return openApi;
    }

    private static void addSchemaForClass(ObjectNode components, Class<?> modelClass, ObjectMapper objectMapper, boolean isArray) {
        ObjectNode schemaNode = objectMapper.createObjectNode();

        if (isArray) {
            if (!components.with(SCHEMAS).has(modelClass.getSimpleName())) {
                addSchemaForClass(components, modelClass, objectMapper, false);
            }

            schemaNode.put("type", ARRAY);
            ObjectNode itemsNode = objectMapper.createObjectNode();
            itemsNode.put("$ref", COMPONENTS_SCHEMAS + modelClass.getSimpleName());
            schemaNode.set("items", itemsNode);

            String listSchemaName = modelClass.getSimpleName() + "List";
            if (!components.with(SCHEMAS).has(listSchemaName)) {
                components.with(SCHEMAS).set(listSchemaName, schemaNode);
            }
        }
        else if (modelClass.isEnum()) {
            // Traiter les enums spécifiquement
            schemaNode.put("type", "string");

            // Récupérer les noms des constantes de l'enum
            ArrayNode enumValues = objectMapper.createArrayNode();
            for (Object constant : modelClass.getEnumConstants()) {
                enumValues.add(constant.toString());
            }
            schemaNode.set("enum", enumValues);

            if (!components.with(SCHEMAS).has(modelClass.getSimpleName())) {
                components.with(SCHEMAS).set(modelClass.getSimpleName(), schemaNode);
            }
        } else {
            schemaNode.put("type", OBJECT);
            ObjectNode properties = objectMapper.createObjectNode();

            for (Field field : modelClass.getDeclaredFields()) {
                ObjectNode fieldSchema = objectMapper.createObjectNode();
                String fieldType = getType(field.getType());
                log.info("Field type: "+ field.getName());
                if (fieldType.equals(OBJECT)) {
                    addSchemaForClass(components, field.getType(), objectMapper, false);
                    fieldSchema.put("$ref", COMPONENTS_SCHEMAS + field.getType().getSimpleName());
                } else if (fieldType.equals(ARRAY)) {
                    Class<?> genericType = getGenericType(field);
                    if (genericType != null) {
                        addSchemaForClass(components, genericType, objectMapper, false);
                        ObjectNode itemsNode = objectMapper.createObjectNode();
                        itemsNode.put("$ref", COMPONENTS_SCHEMAS + genericType.getSimpleName());
                        fieldSchema.put("type", ARRAY);
                        fieldSchema.set("items", itemsNode);
                    } else {
                        fieldSchema.put("type", ARRAY);
                    }
                } else {
                    fieldSchema.put("type", fieldType);
                }

                properties.set(field.getName(), fieldSchema);
            }
            schemaNode.set("properties", properties);

            if (!components.with(SCHEMAS).has(modelClass.getSimpleName())) {
                components.with(SCHEMAS).set(modelClass.getSimpleName(), schemaNode);
            }
        }
    }


    private static Class<?> getGenericType(Field field) {
        try {
            if (List.class.isAssignableFrom(field.getType())) {
                var genericType = (java.lang.reflect.ParameterizedType) field.getGenericType();
                return (Class<?>) genericType.getActualTypeArguments()[0];
            }
        } catch (Exception e) {
            log.severe("Erreur lors de la récupération du type générique" + e);
        }
        return null;
    }

    private static String getType(Class<?> clazz) {
        if (clazz == String.class) return "string";
        if (clazz == Integer.class || clazz == int.class) return "integer";
        if (clazz == Boolean.class || clazz == boolean.class) return "boolean";
        if (clazz == Double.class || clazz == double.class) return "number";
        if (List.class.isAssignableFrom(clazz)) return ARRAY;
        return OBJECT;
    }
}
