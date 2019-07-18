package com.massivecraft.vampire.json;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.vampire.PotionEffectConf;
import com.massivecraft.vampire.altar.AltarDark;
import com.massivecraft.vampire.altar.AltarLight;
import com.massivecraft.vampire.entity.MConf;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.lang.reflect.Type;
import java.util.*;
import java.util.logging.Level;

public class MConfDeserializer implements JsonDeserializer<MConf> {
    @Override
    public MConf deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        MConf mConf = new MConf();
        if (jsonElement.isJsonObject()) {
            JsonObject root = jsonElement.getAsJsonObject();
            Gson gson = new Gson();
            Type listStringType = new TypeToken<List<String>>(){}.getType();
            Type setDCType = new TypeToken<Set<DamageCause>>(){}.getType();
            Type setRRType = new TypeToken<Set<RegainReason>>(){}.getType();
            Type setMaterialType = new TypeToken<Set<Material>>(){}.getType();
            Type mListEntityType = new TypeToken<MassiveList<EntityType>>(){}.getType();

            mConf.setEnabled(root.get("enabled").getAsBoolean());

            mConf.setAliasesVampire(gson.fromJson(root.getAsJsonArray("aliasesVampire"), listStringType));
            mConf.setAliasesVampireShow(gson.fromJson(root.getAsJsonArray("aliasesVampireShow"), listStringType));
            mConf.setAliasesVampireModeBloodlust(gson.fromJson(root.getAsJsonArray("aliasesVampireModeBloodlust"), listStringType));
            mConf.setAliasesVampireModeIntend(gson.fromJson(root.getAsJsonArray("aliasesVampireModeIntend"), listStringType));
            mConf.setAliasesVampireModeNightvision(gson.fromJson(root.getAsJsonArray("aliasesVampireModeNightvision"), listStringType));
            mConf.setAliasesVampireOffer(gson.fromJson(root.getAsJsonArray("aliasesVampireOffer"), listStringType));
            mConf.setAliasesVampireAccept(gson.fromJson(root.getAsJsonArray("aliasesVampireAccept"), listStringType));
            mConf.setAliasesVampireFlask(gson.fromJson(root.getAsJsonArray("aliasesVampireFlask"), listStringType));
            mConf.setAliasesVampireShriek(gson.fromJson(root.getAsJsonArray("aliasesVampireShriek"), listStringType));
            mConf.setAliasesVampireList(gson.fromJson(root.getAsJsonArray("aliasesVampireList"), listStringType));
            mConf.setAliasesVampireSet(gson.fromJson(root.getAsJsonArray("aliasesVampireSet"), listStringType));
            mConf.setAliasesVampireSetVampire(gson.fromJson(root.getAsJsonArray("aliasesVampireSetVampire"), listStringType));
            mConf.setAliasesVampireSetInfection(gson.fromJson(root.getAsJsonArray("aliasesVampireSetInfection"), listStringType));
            mConf.setAliasesVampireSetFood(gson.fromJson(root.getAsJsonArray("aliasesVampireSetFood"), listStringType));
            mConf.setAliasesVampireSetHealth(gson.fromJson(root.getAsJsonArray("aliasesVampireSetHealth"), listStringType));
            mConf.setAliasesVampireEditConfig(gson.fromJson(root.getAsJsonArray("aliasesVampireEditConfig"), listStringType));
            mConf.setAliasesVampireEditLang(gson.fromJson(root.getAsJsonArray("aliasesVampireEditLang"), listStringType));
            mConf.setAliasesVampireVersion(gson.fromJson(root.getAsJsonArray("aliasesVampireVersion"), listStringType));

            mConf.taskDelayMillis = root.get("taskDelayMillis").getAsLong();

            mConf.setCombatDamageFactorWithMcmmoAbilities(root.get("combatDamageFactorWithMcmmoAbilities").getAsBoolean());

            mConf.setFxSmokePerMilli(root.get("fxSmokePerMilli").getAsDouble());
            mConf.setFxEnderPerMilli(root.get("fxEnderPerMilli").getAsDouble());
            mConf.setFxEnderRandomMaxLen(root.get("fxEnderRandomMaxLen").getAsInt());
            mConf.setFxSmokeBurstCount(root.get("fxSmokeBurstCount").getAsDouble());
            mConf.setFxFlameBurstCount(root.get("fxFlameBurstCount").getAsDouble());
            mConf.setFxEnderBurstCount(root.get("fxEnderBurstCount").getAsDouble());

            mConf.setShriekWaitMessageCooldownMillis(root.get("shriekWaitMessageCooldownMillis").getAsLong());
            mConf.setShriekCooldownMillis(root.get("shriekCooldownMillis").getAsLong());

            mConf.setBlockDamageFrom(gson.fromJson(root.getAsJsonArray("blockDamageFrom"), setDCType));
            mConf.setBlockHealthFrom(gson.fromJson(root.getAsJsonArray("blockHealthFrom"), setRRType));

            Map<String, Boolean> upv = new HashMap<>(),
                                 uph = new HashMap<>();
            for(Map.Entry<String, JsonElement> entry : root.getAsJsonObject("updatePermsVampire").entrySet()) {
                upv.put(entry.getKey(), entry.getValue().getAsBoolean());
            }
            for(Map.Entry<String, JsonElement> entry : root.getAsJsonObject("updatePermsHuman").entrySet()) {
                uph.put(entry.getKey(), entry.getValue().getAsBoolean());
            }
            mConf.setUpdatePermsVampire(upv);
            mConf.setUpdatePermsHuman(uph);
            mConf.setUpdateRespawnFood(root.get("updateRespawnFood").getAsInt());
            mConf.setUpdateRespawnHealth(root.get("updateRespawnHealth").getAsInt());
            mConf.setUpdateNameColor(root.get("updateNameColor").getAsBoolean());
            mConf.setUpdateNameColorTo(ChatColor.valueOf(root.get("updateNameColorTo").getAsString()));

            mConf.setDropSelfMaterials(gson.fromJson(root.getAsJsonArray("dropSelfMaterials"), setMaterialType));

            mConf.setBloodlustMinFood(root.get("bloodlustMinFood").getAsDouble());
            mConf.setBloodlustFoodPerMilli(root.get("bloodlustFoodPerMilli").getAsDouble());
            mConf.setBloodlustSmokes(root.get("bloodlustSmokes").getAsDouble());

            mConf.setNightvisionCanBeUsed(root.get("nightvisionCanBeUsed").getAsBoolean());

            mConf.setCanInfectHorses(root.get("canInfectHorses").getAsBoolean());

            Map<PotionEffectType, Integer> etsb = new HashMap<>();
            for(Map.Entry<String, JsonElement> entry : root.getAsJsonObject("effectConfBloodlust").getAsJsonObject("effectToStrength").entrySet()) {
                etsb.put(PotionEffectType.getByName(entry.getKey()), entry.getValue().getAsInt());
            }
            mConf.setEffectConfBloodlust(new PotionEffectConf(EventPriority.valueOf(root.getAsJsonObject("effectConfBloodlust").get("priority").getAsString()),
                                                              root.getAsJsonObject("effectConfBloodlust").get("colorSet").getAsBoolean(),
                                                              root.getAsJsonObject("effectConfBloodlust").get("colorValue").getAsInt(),
                                                              etsb));

            Map<PotionEffectType, Integer> etsn = new HashMap<>();
            for(Map.Entry<String, JsonElement> entry : root.getAsJsonObject("effectConfNightvision").getAsJsonObject("effectToStrength").entrySet()) {
                etsn.put(PotionEffectType.getByName(entry.getKey()), entry.getValue().getAsInt());
            }
            mConf.setEffectConfNightvision(new PotionEffectConf(EventPriority.valueOf(root.getAsJsonObject("effectConfNightvision").get("priority").getAsString()),
                                                              root.getAsJsonObject("effectConfNightvision").get("colorSet").getAsBoolean(),
                                                              root.getAsJsonObject("effectConfNightvision").get("colorValue").getAsInt(),
                                                              etsn));

            Map<PotionEffectType, Integer> etsv = new HashMap<>();
            for(Map.Entry<String, JsonElement> entry : root.getAsJsonObject("effectConfVampire").getAsJsonObject("effectToStrength").entrySet()) {
                etsv.put(PotionEffectType.getByName(entry.getKey()), entry.getValue().getAsInt());
            }
            mConf.setEffectConfVampire(new PotionEffectConf(EventPriority.valueOf(root.getAsJsonObject("effectConfVampire").get("priority").getAsString()),
                                                              root.getAsJsonObject("effectConfVampire").get("colorSet").getAsBoolean(),
                                                              root.getAsJsonObject("effectConfVampire").get("colorValue").getAsInt(),
                                                              etsv));

            Map<PotionEffectType, Integer> etsi = new HashMap<>();
            for(Map.Entry<String, JsonElement> entry : root.getAsJsonObject("effectConfInfected").getAsJsonObject("effectToStrength").entrySet()) {
                etsi.put(PotionEffectType.getByName(entry.getKey()), entry.getValue().getAsInt());
            }
            mConf.setEffectConfInfected(new PotionEffectConf(EventPriority.valueOf(root.getAsJsonObject("effectConfInfected").get("priority").getAsString()),
                                                              root.getAsJsonObject("effectConfInfected").get("colorSet").getAsBoolean(),
                                                              root.getAsJsonObject("effectConfInfected").get("colorValue").getAsInt(),
                                                              etsi));

            Map<PotionEffectType, Integer> etsh = new HashMap<>();
            for(Map.Entry<String, JsonElement> entry : root.getAsJsonObject("effectConfHuman").getAsJsonObject("effectToStrength").entrySet()) {
                etsh.put(PotionEffectType.getByName(entry.getKey()), entry.getValue().getAsInt());
            }
            mConf.setEffectConfHuman(new PotionEffectConf(EventPriority.valueOf(root.getAsJsonObject("effectConfHuman").get("priority").getAsString()),
                                                              root.getAsJsonObject("effectConfHuman").get("colorSet").getAsBoolean(),
                                                              root.getAsJsonObject("effectConfHuman").get("colorValue").getAsInt(),
                                                              etsh));

            mConf.setRegenMinFood(root.get("regenMinFood").getAsDouble());
            mConf.setRegenDelayMillis(root.get("regenDelayMillis").getAsInt());
            mConf.setRegenFoodPerMilli(root.get("regenFoodPerMilli").getAsDouble());
            mConf.setRegenHealthPerFood(root.get("regenHealthPerFood").getAsDouble());

            mConf.setTruceBreakMillis(root.get("truceBreakMillis").getAsLong());
            mConf.setTruceEntityTypes(gson.fromJson(root.getAsJsonArray("truceEntityTypes"), mListEntityType));

            mConf.setCombatDamageFactorWithoutBloodlust(root.get("combatDamageFactorWithoutBloodlust").getAsDouble());
            mConf.setCombatDamageFactorWithBloodlust(root.get("combatDamageFactorWithBloodlust").getAsDouble());
            mConf.setCombatWoodDamage(root.get("combatWoodDamage").getAsInt());
            mConf.setCombatWoodMaterials(gson.fromJson(root.getAsJsonArray("combatWoodMaterials"), setMaterialType));

            mConf.setInfectionPerMilli(root.get("infectionPerMilli").getAsDouble());
            mConf.setInfectionProgressNauseaTicks(root.get("infectionProgressNauseaTicks").getAsInt());
            mConf.setInfectionProgressDamage(root.get("infectionProgressDamage").getAsInt());
            mConf.setInfectionRiskAtCloseCombatWithoutIntent(root.get("infectionRiskAtCloseCombatWithoutIntent").getAsDouble());
            mConf.setInfectionRiskAtCloseCombatWithIntent(root.get("infectionRiskAtCloseCombatWithIntent").getAsDouble());

            mConf.setTradeOfferMaxDistance(root.get("tradeOfferMaxDistance").getAsDouble());
            mConf.setTradeOfferToleranceMillis(root.get("tradeOfferToleranceMillis").getAsLong());
            mConf.setTradeVisualDistance(root.get("tradeVisualDistance").getAsDouble());
            mConf.setTradePercentage(root.get("tradePercentage").getAsDouble());

            mConf.setFoodCakeAllowed(root.get("foodCakeAllowed").getAsBoolean());
            Map<EntityType, Double> etffq = new HashMap<>();
            for(Map.Entry<String, JsonElement> entry : root.getAsJsonObject("entityTypeFullFoodQuotient").entrySet()) {
                etffq.put(EntityType.valueOf(entry.getKey()), entry.getValue().getAsDouble());
            }
            mConf.setEntityTypeFullFoodQuotient(etffq);

            mConf.setHolyWaterSplashRadius(root.get("holyWaterSplashRadius").getAsDouble());
            mConf.setHolyWaterTemp(root.get("holyWaterTemp").getAsDouble());
            List<ItemStack> hwr = new ArrayList<>();
            for(JsonElement rawitem : root.getAsJsonArray("holyWaterResources")) {
                JsonObject jsonitem = rawitem.getAsJsonObject();
                ItemStack item = new ItemStack(Material.getMaterial(jsonitem.get("type").getAsString()), jsonitem.get("amount").getAsInt());
                if (item.getType() == Material.POTION && jsonitem.has("meta")) {
                    PotionMeta pme = (PotionMeta) item.getItemMeta();
                    pme.setBasePotionData(new PotionData(PotionType.valueOf(jsonitem.getAsJsonObject("meta").getAsJsonObject("type").get("type").getAsString()),
                                                         jsonitem.getAsJsonObject("meta").getAsJsonObject("type").get("extended").getAsBoolean(),
                                                         jsonitem.getAsJsonObject("meta").getAsJsonObject("type").get("upgraded").getAsBoolean()));
                    item.setItemMeta(pme);
                }
                hwr.add(item);
            }
            mConf.setHolyWaterResources(hwr);

            mConf.setOpacityPerArmorPiece(root.get("opacityPerArmorPiece").getAsDouble());
            mConf.setBaseRad(root.get("baseRad").getAsDouble());
            mConf.setTempPerRadAndMilli(root.get("tempPerRadAndMilli").getAsDouble());
            mConf.setSunNauseaTemp(root.get("sunNauseaTemp").getAsDouble());
            mConf.setSunWeaknessTemp(root.get("sunWeaknessTemp").getAsDouble());
            mConf.setSunSlowTemp(root.get("sunSlowTemp").getAsDouble());
            mConf.setSunBlindnessTemp(root.get("sunBlindnessTemp").getAsDouble());
            mConf.setSunBurnTemp(root.get("sunBurnTemp").getAsDouble());
            mConf.setSunNauseaTicks(root.get("sunNauseaTicks").getAsInt());
            mConf.setSunWeaknessTicks(root.get("sunWeaknessTicks").getAsInt());
            mConf.setSunSlowTicks(root.get("sunSlowTicks").getAsInt());
            mConf.setSunBlindnessTicks(root.get("sunBlindnessTicks").getAsInt());
            mConf.setSunBurnTicks(root.get("sunBurnTicks").getAsInt());
            mConf.setSunSmokesPerTempAndMilli(root.get("sunSmokesPerTempAndMilli").getAsDouble());
            mConf.setSunFlamesPerTempAndMilli(root.get("sunFlamesPerTempAndMilli").getAsDouble());
            Map<Material, Double> to = new HashMap<>();
            for(Map.Entry<String, JsonElement> entry : root.getAsJsonObject("typeOpacity").entrySet()) {
                to.put(Material.getMaterial(entry.getKey()), entry.getValue().getAsDouble());
            }
            mConf.setTypeOpacity(to);

            mConf.setAltarSearchRadius(root.get("altarSearchRadius").getAsInt());
            mConf.setAltarMinRatioForInfo(root.get("altarMinRatioForInfo").getAsDouble());
            Map<Material, Integer> admc = new HashMap<>(),
                                   almc = new HashMap<>();
            for(Map.Entry<String, JsonElement> entry : root.getAsJsonObject("altarDark").getAsJsonObject("materialCounts").entrySet()) {
                admc.put(Material.getMaterial(entry.getKey()), entry.getValue().getAsInt());
            }
            for(Map.Entry<String, JsonElement> entry : root.getAsJsonObject("altarLight").getAsJsonObject("materialCounts").entrySet()) {
                almc.put(Material.getMaterial(entry.getKey()), entry.getValue().getAsInt());
            }

            List<ItemStack> adr = new ArrayList<>(),
                            alr = new ArrayList<>();
            for(JsonElement rawitem : root.getAsJsonObject("altarDark").getAsJsonArray("resources")) {
                JsonObject jsonitem = rawitem.getAsJsonObject();
                ItemStack item = new ItemStack(Material.getMaterial(jsonitem.get("type").getAsString()), jsonitem.get("amount").getAsInt());
                if (item.getType() == Material.POTION && jsonitem.has("meta")) {
                    PotionMeta pme = (PotionMeta) item.getItemMeta();
                    pme.setBasePotionData(new PotionData(PotionType.valueOf(jsonitem.getAsJsonObject("meta").getAsJsonObject("type").get("type").getAsString()),
                                                         jsonitem.getAsJsonObject("meta").getAsJsonObject("type").get("extended").getAsBoolean(),
                                                         jsonitem.getAsJsonObject("meta").getAsJsonObject("type").get("upgraded").getAsBoolean()));
                    item.setItemMeta(pme);
                }
                adr.add(item);
            }
            for(JsonElement rawitem : root.getAsJsonObject("altarLight").getAsJsonArray("resources")) {
                JsonObject jsonitem = rawitem.getAsJsonObject();
                ItemStack item = new ItemStack(Material.getMaterial(jsonitem.get("type").getAsString()), jsonitem.get("amount").getAsInt());
                if (item.getType() == Material.POTION && jsonitem.has("meta")) {
                    PotionMeta pme = (PotionMeta) item.getItemMeta();
                    pme.setBasePotionData(new PotionData(PotionType.valueOf(jsonitem.getAsJsonObject("meta").getAsJsonObject("type").get("type").getAsString()),
                                                         jsonitem.getAsJsonObject("meta").getAsJsonObject("type").get("extended").getAsBoolean(),
                                                         jsonitem.getAsJsonObject("meta").getAsJsonObject("type").get("upgraded").getAsBoolean()));
                    item.setItemMeta(pme);
                }
                alr.add(item);
            }

            AltarDark ad = new AltarDark();
            AltarLight al = new AltarLight();
            ad.name = root.getAsJsonObject("altarDark").get("name").getAsString();
            al.name = root.getAsJsonObject("altarLight").get("name").getAsString();
            ad.desc = root.getAsJsonObject("altarDark").get("desc").getAsString();
            al.desc = root.getAsJsonObject("altarLight").get("desc").getAsString();
            ad.coreMaterial = Material.getMaterial(root.getAsJsonObject("altarDark").get("coreMaterial").getAsString());
            al.coreMaterial = Material.getMaterial(root.getAsJsonObject("altarLight").get("coreMaterial").getAsString());
            ad.materialCounts = admc;
            al.materialCounts = almc;
            ad.resources = adr;
            al.resources = alr;

            mConf.setAltarDark(ad);
            mConf.setAltarLight(al);

            mConf.setUseWorldGuardRegions(root.get("useWorldGuardRegions").getAsBoolean());
        }

        return mConf;
    }
}
