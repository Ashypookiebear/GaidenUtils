package me.remsq.gaidenUtils;

import java.util.HashMap;
import java.util.UUID;

public class StatManager {
    private class StatData {
        int original;
        int current;

        StatData(int amount) {
            this.original = amount;
            this.current = amount;
        }

        void set(int amount) {
            this.original = amount;
            this.current = amount;
        }

        void minus(int amount) {
            this.current = Math.max(0, this.current - amount);
        }

        void reset() {
            this.current = this.original;
        }
    }

    private final HashMap<UUID, StatData> staminaData = new HashMap<>();
    private final HashMap<UUID, StatData> chakraData = new HashMap<>();

    public void setStat(UUID uuid, String type, int amount) {
        getStatMap(type).put(uuid, new StatData(amount));
    }

    public void minusStat(UUID uuid, String type, int amount) {
        StatData data = getStatMap(type).get(uuid);
        if (data != null) data.minus(amount);
    }

    public void resetStat(UUID uuid, String type) {
        StatData data = getStatMap(type).get(uuid);
        if (data != null) data.reset();
    }

    public int getCurrent(UUID uuid, String type) {
        StatData data = getStatMap(type).get(uuid);
        return (data != null) ? data.current : 0;
    }

    public int getOriginal(UUID uuid, String type) {
        StatData data = getStatMap(type).get(uuid);
        return (data != null) ? data.original : 0;
    }

    private HashMap<UUID, StatData> getStatMap(String type) {
        return type.equalsIgnoreCase("chakra") ? chakraData : staminaData;
    }
}
