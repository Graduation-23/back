package graduation.spendiary.domain.spendingWidget;

import graduation.spendiary.domain.DatabaseSequence.SequenceGeneratorService;
import graduation.spendiary.exception.NoSuchContentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpendingWidgetItemService {
    @Autowired
    private SpendingWidgetItemRepository repo;

    public SpendingWidgetItem getById(Long id) {
        Optional<SpendingWidgetItem> widgetOrNot = repo.findById(id);
        if (widgetOrNot.isEmpty())
            throw new NoSuchContentException();
        return widgetOrNot.get();
    }

    public SpendingWidgetItem save(SpendingWidgetItem item) {
        item.setId(SequenceGeneratorService.generateSequence(SpendingWidgetItem.SEQUENCE_NAME));
        return repo.save(item);
    }
}
