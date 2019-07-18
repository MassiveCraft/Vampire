package com.massivecraft.vampire.json;

import com.google.gson.*;
import com.massivecraft.vampire.entity.MConf;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Type;

public class MConfSerializer implements JsonSerializer<MConf> {
    @Override
    public JsonElement serialize(MConf mConf, Type type, JsonSerializationContext jsonSerializationContext) {
        Gson gson = new Gson();
        JsonObject root = new JsonObject();

        root.addProperty("enabled", mConf.isEnabled());

        root.add("aliasesVampire", gson.toJsonTree(mConf.getAliasesVampire()).getAsJsonArray());
        root.add("aliasesVampireShow", gson.toJsonTree(mConf.getAliasesVampireShow()).getAsJsonArray());
        root.add("aliasesVampireModeBloodlust", gson.toJsonTree(mConf.getAliasesVampireModeBloodlust()).getAsJsonArray());
        root.add("aliasesVampireModeIntend", gson.toJsonTree(mConf.getAliasesVampireModeIntend()).getAsJsonArray());
        root.add("aliasesVampireModeNightvision", gson.toJsonTree(mConf.getAliasesVampireModeNightvision()).getAsJsonArray());
        root.add("aliasesVampireOffer", gson.toJsonTree(mConf.getAliasesVampireOffer()).getAsJsonArray());
        root.add("aliasesVampireAccept", gson.toJsonTree(mConf.getAliasesVampireAccept()).getAsJsonArray());
        root.add("aliasesVampireFlask", gson.toJsonTree(mConf.getAliasesVampireFlask()).getAsJsonArray());
        root.add("aliasesVampireShriek", gson.toJsonTree(mConf.getAliasesVampireShriek()).getAsJsonArray());
        root.add("aliasesVampireList", gson.toJsonTree(mConf.getAliasesVampireList()).getAsJsonArray());
        root.add("aliasesVampireSet", gson.toJsonTree(mConf.getAliasesVampireSet()).getAsJsonArray());
        root.add("aliasesVampireSetVampire", gson.toJsonTree(mConf.getAliasesVampireSetVampire()).getAsJsonArray());
        root.add("aliasesVampireSetInfection", gson.toJsonTree(mConf.getAliasesVampireSetInfection()).getAsJsonArray());
        root.add("aliasesVampireSetFood", gson.toJsonTree(mConf.getAliasesVampireSetFood()).getAsJsonArray());
        root.add("aliasesVampireSetHealth", gson.toJsonTree(mConf.getAliasesVampireSetHealth()).getAsJsonArray());
        root.add("aliasesVampireEditConfig", gson.toJsonTree(mConf.getAliasesVampireEditConfig()).getAsJsonArray());
        root.add("aliasesVampireEditLang", gson.toJsonTree(mConf.getAliasesVampireEditLang()).getAsJsonArray());
        root.add("aliasesVampireVersion", gson.toJsonTree(mConf.getAliasesVampireVersion()).getAsJsonArray());

        root.addProperty("taskDelayMillis", mConf.taskDelayMillis);

        root.addProperty("combatDamageFactorWithMcmmoAbilities", mConf.isCombatDamageFactorWithMcmmoAbilities());

        root.addProperty("fxSmokePerMilli", mConf.getFxSmokePerMilli());
        root.addProperty("fxEnderPerMilli", mConf.getFxEnderPerMilli());
        root.addProperty("fxEnderRandomMaxLen", mConf.getFxEnderRandomMaxLen());
        root.addProperty("fxSmokeBurstCount", mConf.getFxSmokeBurstCount());
        root.addProperty("fxFlameBurstCount", mConf.getFxFlameBurstCount());
        root.addProperty("fxEnderBurstCount", mConf.getFxEnderBurstCount());

        root.addProperty("shriekWaitMessageCooldownMillis", mConf.getShriekWaitMessageCooldownMillis());
        root.addProperty("shriekCooldownMillis", mConf.getShriekCooldownMillis());

        root.add("blockDamageFrom", gson.toJsonTree(mConf.getBlockDamageFrom()).getAsJsonArray());
        root.add("blockHealthFrom", gson.toJsonTree(mConf.getBlockHealthFrom()).getAsJsonArray());

        JsonObject upv = new JsonObject(),
                   uph = new JsonObject();
        for(String key : mConf.getUpdatePermsVampire().keySet()) {
            upv.addProperty(key, mConf.getUpdatePermsVampire().get(key));
        }
        for(String key : mConf.getUpdatePermsHuman().keySet()) {
            uph.addProperty(key, mConf.getUpdatePermsHuman().get(key));
        }
        root.add("updatePermsVampire", upv);
        root.add("updatePermsHuman", uph);
        root.addProperty("updateRespawnFood", mConf.getUpdateRespawnFood());
        root.addProperty("updateRespawnHealth", mConf.getUpdateRespawnHealth());
        root.addProperty("updateNameColor", mConf.isUpdateNameColor());
        root.addProperty("updateNameColorTo", mConf.getUpdateNameColorTo().name());

        root.add("dropSelfMaterials", gson.toJsonTree(mConf.getDropSelfMaterials()).getAsJsonArray());

        root.addProperty("bloodlustMinFood", mConf.getBloodlustMinFood());
        root.addProperty("bloodlustFoodPerMilli", mConf.getBloodlustFoodPerMilli());
        root.addProperty("bloodlustSmokes", mConf.getBloodlustSmokes());

        root.addProperty("nightvisionCanBeUsed", mConf.isNightvisionCanBeUsed());

        root.addProperty("canInfectHorses", mConf.isCanInfectHorses());

        JsonObject ecbb = new JsonObject(),
                   etsb = new JsonObject();
        for(PotionEffectType key : mConf.getEffectConfBloodlust().effectToStrength.keySet()) {
            etsb.addProperty(key.getName(), mConf.getEffectConfBloodlust().effectToStrength.get(key));
        }
        ecbb.addProperty("priority", mConf.getEffectConfBloodlust().priority.name());
        ecbb.addProperty("colorSet", mConf.getEffectConfBloodlust().colorSet);
        ecbb.addProperty("colorValue", mConf.getEffectConfBloodlust().colorValue);
        ecbb.add("effectToStrength", etsb);
        root.add("effectConfBloodlust", ecbb);

        JsonObject ecbn = new JsonObject(),
                   etsn = new JsonObject();
        for(PotionEffectType key : mConf.getEffectConfNightvision().effectToStrength.keySet()) {
            etsn.addProperty(key.getName(), mConf.getEffectConfNightvision().effectToStrength.get(key));
        }
        ecbn.addProperty("priority", mConf.getEffectConfNightvision().priority.name());
        ecbn.addProperty("colorSet", mConf.getEffectConfNightvision().colorSet);
        ecbn.addProperty("colorValue", mConf.getEffectConfNightvision().colorValue);
        ecbn.add("effectToStrength", etsn);
        root.add("effectConfNightvision", ecbn);

        JsonObject ecbv = new JsonObject(),
                   etsv = new JsonObject();
        for(PotionEffectType key : mConf.getEffectConfVampire().effectToStrength.keySet()) {
            etsv.addProperty(key.getName(), mConf.getEffectConfVampire().effectToStrength.get(key));
        }
        ecbv.addProperty("priority", mConf.getEffectConfVampire().priority.name());
        ecbv.addProperty("colorSet", mConf.getEffectConfVampire().colorSet);
        ecbv.addProperty("colorValue", mConf.getEffectConfVampire().colorValue);
        ecbv.add("effectToStrength", etsv);
        root.add("effectConfVampire", ecbv);

        JsonObject ecbi = new JsonObject(),
                   etsi = new JsonObject();
        for(PotionEffectType key : mConf.getEffectConfInfected().effectToStrength.keySet()) {
            etsi.addProperty(key.getName(), mConf.getEffectConfInfected().effectToStrength.get(key));
        }
        ecbi.addProperty("priority", mConf.getEffectConfInfected().priority.name());
        ecbi.addProperty("colorSet", mConf.getEffectConfInfected().colorSet);
        ecbi.addProperty("colorValue", mConf.getEffectConfInfected().colorValue);
        ecbi.add("effectToStrength", etsi);
        root.add("effectConfInfected", ecbi);

        JsonObject ecbh = new JsonObject(),
                   etsh = new JsonObject();
        for(PotionEffectType key : mConf.getEffectConfHuman().effectToStrength.keySet()) {
            etsh.addProperty(key.getName(), mConf.getEffectConfHuman().effectToStrength.get(key));
        }
        ecbh.addProperty("priority", mConf.getEffectConfHuman().priority.name());
        ecbh.addProperty("colorSet", mConf.getEffectConfHuman().colorSet);
        ecbh.addProperty("colorValue", mConf.getEffectConfHuman().colorValue);
        ecbh.add("effectToStrength", etsh);
        root.add("effectConfHuman", ecbh);

        root.addProperty("regenMinFood", mConf.getRegenMinFood());
        root.addProperty("regenDelayMillis", mConf.getRegenDelayMillis());
        root.addProperty("regenFoodPerMilli", mConf.getRegenFoodPerMilli());
        root.addProperty("regenHealthPerFood", mConf.getRegenHealthPerFood());

        root.addProperty("truceBreakMillis", mConf.getTruceBreakMillis());
        root.add("truceEntityTypes", gson.toJsonTree(mConf.getTruceEntityTypes()).getAsJsonArray());

        root.addProperty("combatDamageFactorWithoutBloodlust", mConf.getCombatDamageFactorWithoutBloodlust());
        root.addProperty("combatDamageFactorWithBloodlust", mConf.getCombatDamageFactorWithBloodlust());
        root.addProperty("combatWoodDamage", mConf.getCombatWoodDamage());
        root.add("combatWoodMaterials", gson.toJsonTree(mConf.getCombatWoodMaterials()).getAsJsonArray());

        root.addProperty("infectionPerMilli", mConf.getInfectionPerMilli());
        root.addProperty("infectionProgressNauseaTicks", mConf.getInfectionProgressNauseaTicks());
        root.addProperty("infectionProgressDamage", mConf.getInfectionProgressDamage());
        root.addProperty("infectionRiskAtCloseCombatWithoutIntent", mConf.getInfectionRiskAtCloseCombatWithoutIntent());
        root.addProperty("infectionRiskAtCloseCombatWithIntent", mConf.getInfectionRiskAtCloseCombatWithIntent());

        root.addProperty("tradeOfferMaxDistance", mConf.getTradeOfferMaxDistance());
        root.addProperty("tradeOfferToleranceMillis", mConf.getTradeOfferToleranceMillis());
        root.addProperty("tradeVisualDistance", mConf.getTradeVisualDistance());
        root.addProperty("tradePercentage", mConf.getTradePercentage());

        root.addProperty("foodCakeAllowed", mConf.isFoodCakeAllowed());
        JsonObject etffq = new JsonObject();
        for(EntityType key : mConf.getEntityTypeFullFoodQuotient().keySet()) {
            etffq.addProperty(key.name(), mConf.getEntityTypeFullFoodQuotient().get(key));
        }
        root.add("entityTypeFullFoodQuotient", etffq);

        root.addProperty("holyWaterSplashRadius", mConf.getHolyWaterSplashRadius());
        root.addProperty("holyWaterTemp", mConf.getHolyWaterTemp());
        JsonArray hwr = new JsonArray();
        for (ItemStack is : mConf.getHolyWaterResources()) {
            JsonObject elem = new JsonObject();
            elem.addProperty("type", is.getType().name());
            elem.addProperty("amount", is.getAmount());
            if (is.getType() == Material.POTION && is.hasItemMeta()) {
                PotionMeta pme = (PotionMeta) is.getItemMeta();
                JsonObject meta = new JsonObject(),
                           ptype = new JsonObject();
                ptype.addProperty("type", pme.getBasePotionData().getType().name());
                ptype.addProperty("extended", pme.getBasePotionData().isExtended());
                ptype.addProperty("upgraded", pme.getBasePotionData().isUpgraded());
                meta.add("type", ptype);
                elem.add("meta", meta);
            }
            hwr.add(elem);
        }
        root.add("holyWaterResources", hwr);

        root.addProperty("opacityPerArmorPiece", mConf.getOpacityPerArmorPiece());
        root.addProperty("baseRad", mConf.getBaseRad());
        root.addProperty("tempPerRadAndMilli", mConf.getTempPerRadAndMilli());
        root.addProperty("sunNauseaTemp", mConf.getSunNauseaTemp());
        root.addProperty("sunWeaknessTemp", mConf.getSunWeaknessTemp());
        root.addProperty("sunSlowTemp", mConf.getSunSlowTemp());
        root.addProperty("sunBlindnessTemp", mConf.getSunBlindnessTemp());
        root.addProperty("sunBurnTemp", mConf.getSunBurnTemp());
        root.addProperty("sunNauseaTicks", mConf.getSunNauseaTicks());
        root.addProperty("sunWeaknessTicks", mConf.getSunWeaknessTicks());
        root.addProperty("sunSlowTicks", mConf.getSunSlowTicks());
        root.addProperty("sunBlindnessTicks", mConf.getSunBlindnessTicks());
        root.addProperty("sunBurnTicks", mConf.getSunBurnTicks());
        root.addProperty("sunSmokesPerTempAndMilli", mConf.getSunSmokesPerTempAndMilli());
        root.addProperty("sunFlamesPerTempAndMilli", mConf.getSunFlamesPerTempAndMilli());
        JsonObject to = new JsonObject();
        for(Material key : mConf.getTypeOpacity().keySet()) {
            to.addProperty(key.name(), mConf.getTypeOpacity().get(key));
        }
        root.add("typeOpacity", to);

        root.addProperty("altarSearchRadius", mConf.getAltarSearchRadius());
        root.addProperty("altarMinRatioForInfo", mConf.getAltarMinRatioForInfo());
        JsonObject ad = new JsonObject(),
                   al = new JsonObject();
        ad.addProperty("name", mConf.getAltarDark().name);
        al.addProperty("name", mConf.getAltarLight().name);
        ad.addProperty("desc", mConf.getAltarDark().desc);
        al.addProperty("desc", mConf.getAltarLight().desc);
        ad.addProperty("coreMaterial", mConf.getAltarDark().coreMaterial.name());
        al.addProperty("coreMaterial", mConf.getAltarLight().coreMaterial.name());
        JsonObject admc = new JsonObject(),
                   almc = new JsonObject();
        for(Material key : mConf.getAltarDark().materialCounts.keySet()) {
            admc.addProperty(key.name(), mConf.getAltarDark().materialCounts.get(key));
        }
        for(Material key : mConf.getAltarLight().materialCounts.keySet()) {
            almc.addProperty(key.name(), mConf.getAltarLight().materialCounts.get(key));
        }
        ad.add("materialCounts", admc);
        al.add("materialCounts", almc);
        JsonArray adr = new JsonArray(),
                  alr = new JsonArray();
        for (ItemStack is : mConf.getAltarDark().resources) {
            JsonObject elem = new JsonObject();
            elem.addProperty("type", is.getType().name());
            elem.addProperty("amount", is.getAmount());
            if (is.getType() == Material.POTION && is.hasItemMeta()) {
                PotionMeta pme = (PotionMeta) is.getItemMeta();
                JsonObject meta = new JsonObject(),
                        ptype = new JsonObject();
                ptype.addProperty("type", pme.getBasePotionData().getType().name());
                ptype.addProperty("extended", pme.getBasePotionData().isExtended());
                ptype.addProperty("upgraded", pme.getBasePotionData().isUpgraded());
                meta.add("type", ptype);
                elem.add("meta", meta);
            }
            adr.add(elem);
        }
        for (ItemStack is : mConf.getAltarLight().resources) {
            JsonObject elem = new JsonObject();
            elem.addProperty("type", is.getType().name());
            elem.addProperty("amount", is.getAmount());
            if (is.getType() == Material.POTION && is.hasItemMeta()) {
                PotionMeta pme = (PotionMeta) is.getItemMeta();
                JsonObject meta = new JsonObject(),
                        ptype = new JsonObject();
                ptype.addProperty("type", pme.getBasePotionData().getType().name());
                ptype.addProperty("extended", pme.getBasePotionData().isExtended());
                ptype.addProperty("upgraded", pme.getBasePotionData().isUpgraded());
                meta.add("type", ptype);
                elem.add("meta", meta);
            }
            alr.add(elem);
        }
        ad.add("resources", adr);
        al.add("resources", alr);
        root.add("altarDark", ad);
        root.add("altarLight", al);

        root.addProperty("useWorldGuardRegions", mConf.isUseWorldGuardRegions());

        return root;
    }
}
