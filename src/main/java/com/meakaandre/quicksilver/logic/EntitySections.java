package com.meakaandre.quicksilver.logic;

/**
 * P2-2, logic.entity-sections: entity sections touching a small box looked up directly, in vanilla's order (Lithium's
 * fast_retrieval; {@code mixin/logic/EntitySectionStorageMixin}).
 */
public final class EntitySections {
    public static final String ID = "entity-sections";
    public static final Logic.Switch SWITCH = new Logic.Switch(ID);

    private EntitySections() {
    }
}
