package graduation.spendiary.domain.spendingWidget;

import lombok.*;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDate;
import java.util.List;

@Document(collection = "SpendingWidget")
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SpendingWidget {
    @MongoId
    private Long id;

    @Field("user_id")
    private String userId;

    @Field("date")
    private LocalDate date;

    @Field("item_ids")
    private List<Long> itemIds;

    @Field("total_cost")
    private long totalCost;
}
