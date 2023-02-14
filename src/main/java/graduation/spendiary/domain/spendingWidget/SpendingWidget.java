package graduation.spendiary.domain.spendingWidget;

import lombok.*;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Document(collection = "SpendingWidgets")
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SpendingWidget {
    @Transient
    public static final String SEQUENCE_NAME = "spending_widget_sequence";

    @MongoId
    private Long id;

    @Field("diary_id")
    private Long diaryId;

    @Field("item_ids")
    private List<Long> itemIds;

    @Field("total_cost")
    private long totalCost;
}
