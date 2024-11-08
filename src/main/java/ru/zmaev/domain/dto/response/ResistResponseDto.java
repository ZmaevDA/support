package ru.zmaev.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResistResponseDto {
    private Long id;
    private Integer vitality;
    private Integer focus;
    private Integer robustness;
    private Integer immunity;
    private Integer holy;
    private Integer lightning;
    private Integer fire;
    private Integer magic;
    private Integer pierce;
    private Integer splash;
    private Integer strike;
    private Integer physical;

    public void increaseVitality(int value) {
        if (vitality == null) {
            vitality = value;
        } else {
            vitality += value;
        }
    }

    public void increaseFocus(int value) {
        if (focus == null) {
            focus = value;
        } else {
            focus += value;
        }
    }

    public void increaseRobustness(int value) {
        if (robustness == null) {
            robustness = value;
        } else {
            robustness += value;
        }
    }

    public void increaseImmunity(int value) {
        if (immunity == null) {
            immunity = value;
        } else {
            immunity += value;
        }
    }

    public void increaseHoly(int value) {
        if (holy == null) {
            holy = value;
        } else {
            holy += value;
        }
    }

    public void increaseLightning(int value) {
        if (lightning == null) {
            lightning = value;
        } else {
            lightning += value;
        }
    }

    public void increaseFire(int value) {
        if (fire == null) {
            fire = value;
        } else {
            fire += value;
        }
    }

    public void increaseMagic(int value) {
        if (magic == null) {
            magic = value;
        } else {
            magic += value;
        }
    }

    public void increasePierce(int value) {
        if (pierce == null) {
            pierce = value;
        } else {
            pierce += value;
        }
    }

    public void increaseSplash(int value) {
        if (splash == null) {
            splash = value;
        } else {
            splash += value;
        }
    }

    public void increaseStrike(int value) {
        if (strike == null) {
            strike = value;
        } else {
            strike += value;
        }
    }

    public void increasePhysical(int value) {
        if (physical == null) {
            physical = value;
        } else {
            physical += value;
        }
    }
}
