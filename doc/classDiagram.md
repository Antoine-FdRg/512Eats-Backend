# Diagramme de classe

```mermaid
classDiagram
    direction TB
    class Location {
        - Location(LocationBuilder)
        - String streetNumber
        - int id
        - String address
        - String city
        + equals(Object) boolean
        + hashCode() int
        + getId() int
        + getStreetNumber() String
        + getAddress() String
        + getCity() String
        + setStreetNumber(String) void
        + setAddress(String) void
        + setCity(String) void
        + toString() String
    }
    class LocationBuilder {
        + LocationBuilder()
        - String city
        - String streetNumber
        - int id
        - String address
        - int idCounter$
        + build() Location
        + setNumber(String) LocationBuilder
        + setCity(String) LocationBuilder
        + setAddress(String) LocationBuilder
    }
    class Payment {
        + Payment(double, DateTime)
        - double amount
        - DateTime time
        + getAmount() double
        + getTime() DateTime
        + setAmount(double) void
        + setTime(DateTime) void
        + equals(Object) boolean
        + hashCode() int
        + toString() String
    }
    class PaymentProcessor {
        + PaymentProcessor()
        + processPayment(double) boolean
    }
    note for PaymentProcessor "cette classe est l'interface vers l'acteur externe"
    class RegisteredUser {
        + RegisteredUser(int, String, Role, SubOrder, List~SubOrder~)
        + RegisteredUser(String, Role)
        - int idCounter$
        - String name
        - int id
        - Role role
        + getName() String
        + getId() int
        + setOrders(List~SubOrder~) void
        + getRole() Role
        + getCurrentOrder() SubOrder
        + getOrders() List~SubOrder~
        + setId(int) void
        + setName(String) void
        + setRole(Role) void
        + setCurrentOrder(SubOrder) void
        + addOrderToHistory(SubOrder) boolean
        + initializeIndividualOrder(Restaurant, Location, DateTime) SubOrder
    }
    class Role {
        <<enumeration>>
        + Role()
        +  STUDENT
        +  RESTAURANT_MANAGER
        +  CAMPUS_EMPLOYEE
        + canOrder() boolean
        + values() Role[]
        + valueOf(String) Role
    }
    namespace package Order {
        class SubOrder {
            ~ SubOrder(int, double, GroupOrder, Restaurant, RegisteredUser, List~Dish~, OrderStatus, DateTime, DateTime)
            - DateTime deliveryDate
            - int id
            - int userID
            - int restaurantID
            - DateTime placedDate
            - double price
            + getId() int
            + getPrice() double
            + setPayment(Payment) void
            + setDishes(List~Dish~) void
            + setPlacedDate(DateTime) void
            + getGroupOrder() GroupOrder
            + getRestaurant() Restaurant
            + getUser() RegisteredUser
            + setStatus(OrderStatus) void
            + getDishes() List~Dish~
            + setGroupOrder(GroupOrder) void
            + getStatus() OrderStatus
            + setRestaurant(Restaurant) void
            + setDeliveryDate(DateTime) void
            + setPrice(double) void
            + getPlacedDate() DateTime
            + getDeliveryDate() DateTime
            + setId(int) void
            + getPayment() Payment
            + setUser(RegisteredUser) void
            + cancel() void
            + addDish(Dish) boolean
            + place() void
            + getPreparationTime() int
            + getCheaperDish() Dish
            + pay() void
        }
        class GroupOrder {
            + GroupOrder(int, Date, OrderStatus, List~SubOrder~, Location)
            - GroupOrder(GroupOrderBuilder)
            - int id
            - Location deliveryLocation
            - OrderStatus status
            - Date deliveryDate
            + getId() int
            + getDeliveryDateTime() deliveryDate
            + getStatus() OrderStatus
            + getSubOrders() List~SubOrder~
            + getDeliveryLocation() Location
            + setDate(Date) void
            + setStatus(OrderStatus) void
            + setSubOrders(List~SubOrder~) void
            + setDeliveryLocation(Location) void
            + close() void
            + addSubOrder(SubOrder) boolean
        }
        class GroupOrderBuilder {
            + GroupOrderBuilder()
            - int nextId$
            - int id
            - Date date
            + withSubOrders(List~SubOrder~) GroupOrderBuilder
            + withDeliveryLocation(Location) GroupOrderBuilder
            + build() GroupOrder
            + withDate(Date) GroupOrderBuilder
        }
        class OrderBuilder {
            + OrderBuilder()
            - double price
            - DateTime deliveryTime
            - int id
            - int idCounter$
            + build() SubOrder
            + setPrice(double) OrderBuilder
            + setDeliveryLocation(Location) OrderBuilder
            + setUser(RegisteredUser) OrderBuilder
            + setGroupOrder(GroupOrder) OrderBuilder
            + setRestaurant(Restaurant) OrderBuilder
            + setDeliveryTime(DateTime) OrderBuilder
        }
        class IndividualOrder {
            ~ IndividualOrder(int, double, GroupOrder, Restaurant, RegisteredUser, List~Dish~, OrderStatus, DateTime, DateTime, Location)
            + getDeliveryLocation() Location
            + setDeliveryLocation(Location) void
            + pay() void
        }

        class OrderStatus {
            <<enumeration>>
            + OrderStatus()
            +  PAID
            +  DISCOUNT_USED
            +  PLACED
            +  DELIVERING
            +  CREATED
            +  CANCELED
            +  COMPLETED
            + valueOf(String) OrderStatus
            + values() OrderStatus[]
        }
    }

    namespace package Restaurant {
        class Restaurant {
            - Restaurant(RestaurantBuilder)
            - int id
            - String name
            - LocalTime open
            - int averageOrderPreparationTime
            - LocalTime close
            + getId() int
            + getName() String
            + getOpen() LocalTime
            + getClose() LocalTime
            + setFoodTypes(List~FoodType~) void
            + getTimeSlots() List~TimeSlot~
            + setClose(LocalTime) void
            + getDishes() List~Dish~
            + setName(String) void
            + setAverageOrderPreparationTime(int) void
            + setTimeSlots(List~TimeSlot~) void
            + getFoodTypes() List~FoodType~
            + getDiscountStrategy() DiscountStrategy
            + setDishes(List~Dish~) void
            + setId(int) void
            + getAverageOrderPreparationTime() int
            + setDiscountStrategy(DiscountStrategy) void
            + setOpen(LocalTime) void
            + getCurrentTimeSlot(DateTime) TimeSlot
            + getAvailableDeliveryTimesOnDay(LocalDate) List~DateTime~
            + getPreviousTimeSlot(DateTime) TimeSlot
            + addDish(Dish) void
            + getDishesReadyInLessThan(int) List~Dish~
            + isAvailable(DateTime) boolean
            + removeDish(int) void
            + addOrderToTimeslot(SubOrder) void
            + addTimeSlot(TimeSlot) void
        }
        class RestaurantBuilder {
            + RestaurantBuilder()
            - String name
            - int idCounter$
            - LocalTime open
            - int averageOrderPreparationTime
            - int id
            - LocalTime close
            + build() Restaurant
            + setName(String) RestaurantBuilder
            + setOpen(LocalTime) RestaurantBuilder
            + setClose(LocalTime) RestaurantBuilder
            + setAverageOrderPreparationTime(int) RestaurantBuilder
            + setFoodTypes(List~FoodType~) RestaurantBuilder
        }
        class Dish {
            - Dish(DishBuilder)
            - String name
            - double price
            - int preparationTime
            - String picture
            - String description
            - int id
            + getId() int
            + getName() String
            + getDescription() String
            + setPicture(String) void
            + getPrice() double
            + getPreparationTime() int
            + getPicture() String
            + setId(int) void
            + setName(String) void
            + setDescription(String) void
            + setPrice(double) void
            + setPreparationTime(int) void
            + hashCode() int
            + equals(Object) boolean
            + toString() String
        }
        class TimeSlot {
            + TimeSlot(int, List~SubOrder~, DateTime, Restaurant, int, int)
            + TimeSlot(DateTime, Restaurant, int)
            - int id
            - int productionCapacity
            - DateTime start
            - int idCounter$
            - int maxNumberOfOrders
            + int DURATION
            + getRestaurant() Restaurant
            + getId() int
            + getOrders() List~SubOrder~
            + getStart() DateTime
            + getProductionCapacity() int
            + getMaxNumberOfOrders() int
            + setId(int) void
            + setMaxNumberOfOrders(int) void
            + setProductionCapacity(int) void
            + setOrders(List~SubOrder~) void
            + setRestaurant(Restaurant) void
            + toString() String
            + setStart(DateTime) void
            + getTotalMaxPreparationTime() int
            - getNumberOfPlacedOrders() int
            + getFreeProductionCapacity() int
            + addOrder(SubOrder) void
            + getNumberOfCreatedOrders() int
            + isFull() boolean
            - getTotalPreparationTime() int
            + getEnd() DateTime
        }

        class FoodType {
            <<enumeration>>
            - FoodType(String)
            +  PIZZA
            +  VEGAN
            +  POKEBOWL
            +  BURGER
            +  SANDWICH
            +  FAST_FOOD
            +  ASIAN_FOOD
            +  TACOS
            ~ String name
            +  SUSHI
            + getName() String
            + values() FoodType[]
            + valueOf(String) FoodType
        }

        class DishBuilder {
            + DishBuilder()
            - int id
            - String picture
            - int preparationTime
            - int idCounter$
            - double price
            - String description
            - String name
            + setName(String) DishBuilder
            + setId(int) DishBuilder
            + setPicture(String) DishBuilder
            + setPreparationTime(int) DishBuilder
            + build() Dish
            + setPrice(double) DishBuilder
            + setDescription(String) DishBuilder
        }
    }
    namespace package Service {
        class ManageRestaurantService {
            + ManageRestaurantService(RestaurantRepository)
            + ManageRestaurantService()
            + String RESTAURANT_NOT_FOUND
            + addDish(int, String, String, double, int) void
            + restaurantValidator(int) Restaurant
            + updateRestaurantInfos(int, String, String) void
            + updateDish(int, int, double, int) void
            + removeDish(int, int) void
        }
        class OrderService {
            + OrderService(GroupOrderRepository, LocationRepository, SubOrderRepository, RestaurantRepository, RegisteredUserRepository, PaymentProcessor)
            + OrderService(GroupOrderRepository, LocationRepository, SubOrderRepository, RestaurantRepository, RegisteredUserRepository)
            + getGroupOrderRepository() GroupOrderRepository
            + getAvailableDishes(int, int) List~Dish~
            + getGroupOrderById(int) GroupOrder
            + createGroupOrder(int) void
            + createIndividualOrder(int, int, int, DateTime, DateTime) void
            + createOrderInGroup(int, int, int) void
            + addDishToOrder(int, int) void
            + paySubOrder(int, int, DateTime) void
            + placeSubOrder(int) void
            + registeredUserValidator(int) RegisteredUser
        }
        class RestaurantService {
            + RestaurantService()
            + addRestaurant(Restaurant) void
            + deleteRestaurant(Restaurant) void
            + addDishToRestaurant(Restaurant, Dish) void
            + addTimeSlotToRestaurant(int, int) void
            + getAllDishesFromRestaurant(String) List~Dish~
            + getAllAvailableDeliveryTimesOfRestaurantOnDay(int, LocalDate) List~DateTime~
            + getAvailableRestaurantsForGroupOrder(int) List~Restaurant~
            - getRestaurantOrThrowIfNull(Restaurant) Restaurant
            + getRestaurantByName(String) Restaurant
            + getRestaurantsByName(String) List~Restaurant~
            + verifyRestaurantAvailabilityForSubOrder(int) boolean
            + getRestaurantsByAvailability(DateTime) List~Restaurant~
            + getRestaurantsByFoodType(List~FoodType~) List~Restaurant~
            + getAllRestaurants() List~Restaurant~
        }
    }
    namespace package Repository {
        class DishRepository {
            + DishRepository()
            + add(Dish) void
            + findAll() List~Dish~
            + clear() void
            + findById(int) Dish
            + remove(Dish) void
        }
        class GroupOrderRepository {
            + GroupOrderRepository()
            + add(GroupOrder) void
            + getGroupOrders() List~GroupOrder~
            + findGroupOrderById(int) GroupOrder
        }
        class IndividualOrderRepository {
            + IndividualOrderRepository()
            + add(IndividualOrder) void
            + delete(IndividualOrder) void
            + findAll() List~IndividualOrder~
            + findById(int) IndividualOrder
        }
        class LocationRepository {
            + LocationRepository()
            + getLocations() List~Location~
            + add(Location) void
            + findLocationById(int) Location
        }
        class RegisteredUserRepository {
            + RegisteredUserRepository()
            + findById(int) RegisteredUser
            + delete(RegisteredUser) void
            + findAll() List~RegisteredUser~
            + clear() void
            + add(RegisteredUser) void
        }
        class RestaurantRepository {
            + RestaurantRepository()
            + findAll() List~Restaurant~
            + add(Restaurant) void
            + findRestaurantsByAvailability(DateTime) List~Restaurant~
            + findByName(String) Restaurant
            + delete(Restaurant) void
            + findRestaurantByFoodType(List~FoodType~) List~Restaurant~
            + findRestaurantByName(String) List~Restaurant~
            + findById(int) Restaurant
        }
        class SubOrderRepository {
            + SubOrderRepository()
            - List~SubOrder~ subOrders
            + findAll() List~SubOrder~
            + clear() void
            + findById(int) SubOrder
            + delete(SubOrder) void
            + add(SubOrder) void
            + findByUserId(int) SubOrder
        }
        class TimeSlotRepository {
            + TimeSlotRepository()
            + findById(int) TimeSlot
            + add(TimeSlot) void
            + removeTimeSlot(TimeSlot) void
            + clear() void
            + findAll() List~TimeSlot~
        }
    }
    namespace package  Discount {
        class DiscountStrategy {
            <<Abstract>>
            + DiscountStrategy(Restaurant)
            # int restaurantID
            + applyDiscount(SubOrder, RegisteredUser)* double
        }
        class UnconditionalDiscount {
            + UnconditionalDiscount(Restaurant, double)
            - double discountRate
            + applyDiscount(SubOrder, RegisteredUser) double
        }
        class RoleDiscount {
            + RoleDiscount(Restaurant, double, Role)
            - double discountRate
            - Role role
            + applyDiscount(SubOrder, RegisteredUser) double
        }

        class FreeDishAfterXOrders {
            + FreeDishAfterXOrders(Restaurant, int)
            - int nbOrdersRequired
            + applyDiscount(SubOrder) double
            - getNbOrderInRestaurant(RegisteredUser) int
        }
    }

    DiscountStrategy "discountStrategy 1" <-- "1" Restaurant
    DishRepository "1" --> "* dishes" Dish
    FreeDishAfterXOrders --|> DiscountStrategy
    GroupOrder "1" --> "deliveryLocation 1" Location
    GroupOrder "1" --> "status 1" OrderStatus
    GroupOrder "1" --> "subOrders *" SubOrder
    GroupOrderBuilder "1" --> "deliveryLocation 1" Location
    GroupOrderBuilder "1" --> "status 1" OrderStatus
    GroupOrderBuilder "1" --> "subOrders *" SubOrder
    GroupOrderRepository "1" --> "groupOrders *" GroupOrder
    IndividualOrder "1" --> "deliveryLocation 1" Location
    IndividualOrder --|> SubOrder
    IndividualOrderRepository "1" --> "individualOrders *" IndividualOrder
    LocationRepository "1" --> "locations *" Location
    ManageRestaurantService "1" --> "restaurantRepository 1" RestaurantRepository
    OrderBuilder "1" --> "dishes *" Dish
    OrderBuilder "1" --> "groupOrder 1" GroupOrder
    OrderBuilder "1" --> "deliveryLocation 1" Location
    OrderBuilder "1" --> "user 1" RegisteredUser
    OrderBuilder "1" --> "restaurant 1" Restaurant
    OrderService "1" --> "groupOrderRepository 1" GroupOrderRepository
    OrderService "1" --> "locationRepository 1" LocationRepository
    OrderService "1" --> "paymentProcessor 1" PaymentProcessor
    OrderService "1" --> "registeredUserRepository 1" RegisteredUserRepository
    OrderService "1" --> "restaurantRepository 1" RestaurantRepository
    OrderService "1" --> "subOrderRepository 1" SubOrderRepository
    RegisteredUser "1" --> "role 1" Role
    RegisteredUser "1" --> "orders *" SubOrder
    RegisteredUserRepository "1" --> "registeredUsers *" RegisteredUser
    Restaurant "1" --> "dishes *" Dish
    Restaurant "1" --> "foodTypes *" FoodType
    Restaurant "1" --> "timeSlots *" TimeSlot
    RestaurantBuilder "1" --> "discountStrategy 1" DiscountStrategy
    RestaurantBuilder "1" --> "dishes *" Dish
    RestaurantBuilder "1" --> "foodTypes *" FoodType
    RestaurantBuilder "1" --> "timeSlots *" TimeSlot
    RestaurantRepository "1" --> "restaurants *" Restaurant
    RestaurantService "1" --> "restaurantRepository 1" RestaurantRepository
    RestaurantService "1" --> "timeSlotRepository 1" TimeSlotRepository
    RoleDiscount --|> DiscountStrategy
    RoleDiscount "1" --> "role 1" Role
    SubOrder "1" --> "dishes *" Dish
    SubOrder "1" --> "groupOrder 1" GroupOrder
    SubOrder "1" --> "status 1" OrderStatus
    SubOrder "1" --> "payment 1" Payment
    SubOrderRepository "1" --> "subOrders *" SubOrder
    TimeSlot "1" --> "orders *" SubOrder
    TimeSlotRepository "1" --> "timeSlots *" TimeSlot
    UnconditionalDiscount --|> DiscountStrategy 
```
