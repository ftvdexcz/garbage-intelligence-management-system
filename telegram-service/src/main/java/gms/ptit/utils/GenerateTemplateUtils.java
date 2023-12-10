package gms.ptit.utils;

import gms.ptit.dto.event.KafkaEventLoadCell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class GenerateTemplateUtils {
    @Autowired
    static
    TemplateEngine templateEngine;

    public static String generateLoadCellEventTemplate(KafkaEventLoadCell kafkaEventLoadCell){
        Context context = new Context();
        context.setVariable("bin_id", kafkaEventLoadCell.getBinId());
        context.setVariable("company", kafkaEventLoadCell.getCompany());
        context.setVariable("lat", kafkaEventLoadCell.getLat());
        context.setVariable("lon", kafkaEventLoadCell.getLon());
        context.setVariable("weight", kafkaEventLoadCell.getWeight());
        context.setVariable("total_weight", kafkaEventLoadCell.getTotalWeight() / 1000);
        context.setVariable("timestamp", DateUtils.formattedTimestamp(kafkaEventLoadCell.getTimestamp()));

        return templateEngine.process("load-cell", context);
    }
}
