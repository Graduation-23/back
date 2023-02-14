package graduation.spendiary.domain.spendingWidget;

import lombok.*;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "SpendingWidgetsItems")
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SpendingWidgetItem {
    @Transient
    public static final String SEQUENCE_NAME = "spending_widget_item_sequence";

    @MongoId
    private Long id;

    @Field("finance_id")
    private Long financeId;

    @Field("item_amount")
    private Long amount;

    @Field("item_description")
    private String description;

    @Field("item_category")
    private String category;
}
