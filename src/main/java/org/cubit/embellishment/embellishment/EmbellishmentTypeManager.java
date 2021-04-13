package org.cubit.embellishment.embellishment;

import lombok.Getter;
import org.cubit.embellishment.api.IEmbellishmentType;

import java.util.HashMap;
import java.util.Map;

@Getter       
public class EmbellishmentTypeManager {

    private Map<String , IEmbellishmentType> embellishmentType;

    public EmbellishmentTypeManager() {
        this.embellishmentType = new HashMap<>();
    }

    public IEmbellishmentType register(String name , IEmbellishmentType iEmbellishmentType) {
       return this.embellishmentType.put(name , iEmbellishmentType);
    }

    public IEmbellishmentType getEmbellishmentType(String name) {
        return this.embellishmentType.get(name);
    }



}
