package ralseiii.skyfabric.hud;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import ralseiii.skyfabric.mixin.PlayerHudAccessor;
import ralseiii.skyfabric.utils.SbChecks;

import java.util.ArrayList;
import java.util.List;

public class MiningQuestHud {
    static List<String> questList = new ArrayList<>();
    static String[] quests = {
            " Crystal Hunter",
            " Gemstone Collector",
            "Titanium",
            "Mithril",
            "Hard Stone Miner",
            "Ice Walker Slayer",
            "Yog Slayer",
            "Team Treasurite Member Slayer",
            "Sludge Slayer",
            "Boss Corleone Slayer",
            "Star Sentry Puncher",
            "2x Mithril Powder Collector",
            "Chest Looter",
            "Thyst Slayer"

    };
    static String mithrilPowderString = "";
    static String gemstonePowderString = "";

    public static void register() {
        HudRenderCallback.EVENT.register(((matrixStack, tickDelta) -> {
            if (!SbChecks.isCrystalHollows) return;
            var client = MinecraftClient.getInstance();
            var tx = client.textRenderer;
            var y = 125f;
            for (var s : questList) {
                matrixStack.push();
                matrixStack.scale(1f, 1f, 1f);
                tx.draw(matrixStack, s, 100, y, 0xffffff);
                y += 16f;
                matrixStack.pop();
            }
        }));
    }

    static boolean checkIsQuest(String str) {
        for (var s : quests) {
            if (str.contains(s)) return !str.contains("Mithril Powder:");
        }
        return false;
    }

    public static void update() {
        if (!SbChecks.isCrystalHollows) return;
        List<String> tempQuestList = new ArrayList<>();
        var client = MinecraftClient.getInstance();
        if (client == null || client.world == null) return;
        if (client.player == null) return;
        var playerListEntryList = client.player.networkHandler.getPlayerList();
        for (var entry : playerListEntryList) {
            var playerNameTextSiblingList = ((PlayerHudAccessor) client.inGameHud.getPlayerListHud()).invokeGetPlayerName(entry).getSiblings();
            if (playerNameTextSiblingList != null && !playerNameTextSiblingList.isEmpty()) {
                var s = playerNameTextSiblingList.get(0).getString();
                if (playerNameTextSiblingList.size() > 2) {
                    if (s.contains("Mithril Powder")) {
                        mithrilPowderString = playerNameTextSiblingList.get(1).getString() + playerNameTextSiblingList.get(2).getString();
                    } else if (s.contains("Gemstone Powder")) {
                        gemstonePowderString = playerNameTextSiblingList.get(1).getString() + playerNameTextSiblingList.get(2).getString();
                    } else if (checkIsQuest(playerNameTextSiblingList.get(1).getString())) {
                        tempQuestList.add(playerNameTextSiblingList.get(1).getString() + playerNameTextSiblingList.get(2).getString());
                        // System.out.println(playerNameTextSiblingList);
                    }
                }
            }
        }
        // System.out.println(tempQuestList);
        questList = tempQuestList;
    }
}
