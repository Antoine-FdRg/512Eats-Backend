package commonlibrary.dto;

import java.util.List;

public record RestaurantDTO(int id, String name, String openTime, String closeTime, List<String> foodTypeList,
                            double averagePrice, String description, List<String> dishPictureURLListSample) {
}
