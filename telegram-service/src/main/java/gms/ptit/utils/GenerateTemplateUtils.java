package gms.ptit.utils;

import gms.ptit.dto.event.KafkaEventCheckPlate;
import gms.ptit.dto.event.KafkaEventLoadCell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class GenerateTemplateUtils {
    @Autowired
    TemplateEngine templateEngine;

    public String generateLoadCellEventTemplate(KafkaEventLoadCell kafkaEventLoadCell){
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

    public String generateCheckPlateEventTemplate(KafkaEventCheckPlate kafkaEventCheckPlate){
        Context context = new Context();
        context.setVariable("checks", kafkaEventCheckPlate.getChecks());
        context.setVariable("timestamp", DateUtils.formattedTimestamp(kafkaEventCheckPlate.getTimestamp()));
        context.setVariable("bin_id", kafkaEventCheckPlate.getBinId());
        context.setVariable("company", kafkaEventCheckPlate.getCompany());
        context.setVariable("lat", kafkaEventCheckPlate.getLat());
        context.setVariable("lon", kafkaEventCheckPlate.getLon());
        return templateEngine.process("check-plate", context);
    }
}
