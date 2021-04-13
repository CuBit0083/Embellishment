package org.cubit.embellishment.comand;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.cubit.embellishment.EmbellishmentCore;
import org.cubit.embellishment.EmbellishmentUIManager;
import org.cubit.embellishment.api.IEmbellishmentType;
import org.cubit.embellishment.config.EmbellishmentTypeConfig;
import org.cubit.embellishment.embellishment.EmbellishmentManager;
import org.cubit.embellishment.embellishment.EmbellishmentTypeManager;
import skywolf46.commandannotation.annotations.legacy.MinecraftCommand;
import skywolf46.commandannotation.data.command.CommandArgument;

import java.util.List;

public class EmbellishmentCommand {

    private EmbellishmentManager embellishmentManager;
    private EmbellishmentTypeManager embellishmentTypeManager;
    private EmbellishmentTypeConfig embellishmentTypeConfig;
    private EmbellishmentUIManager embellishmentUIManager;

    public EmbellishmentCommand(EmbellishmentManager embellishmentManager, EmbellishmentTypeManager embellishmentTypeManager, EmbellishmentTypeConfig embellishmentTypeConfig , EmbellishmentUIManager embellishmentUIManager) {
        this.embellishmentManager = embellishmentManager;
        this.embellishmentTypeManager = embellishmentTypeManager;
        this.embellishmentTypeConfig = embellishmentTypeConfig;
        this.embellishmentUIManager = embellishmentUIManager;
    }

    @MinecraftCommand("/치장")
    public void Embellishment(Player player, CommandArgument args) {
        this.embellishmentUIManager.openInventory(player);
    }

    @MinecraftCommand("/치장 도움말")
    public void helpEmbellishment(Player player, CommandArgument args) {
        if (!player.isOp()) {
            player.sendMessage(EmbellishmentCore.suffix + "당신은 권한이 없습니다.");
            return;
        }
        player.sendMessage(EmbellishmentCore.suffix + "./치장 얻기 [플레이어 닉네임] [치장 이름]");
        player.sendMessage(EmbellishmentCore.suffix + "./치장 삭제 [플레이어 닉네임] [치장 이름]");
        player.sendMessage(EmbellishmentCore.suffix + "./치장 초기화 [플레이어 닉네임]");
        player.sendMessage(EmbellishmentCore.suffix + "./치장 리로드");

    }

    @MinecraftCommand("/치장 얻기")
    public void giveEmbellishment(Player player, CommandArgument args) {
        try {
            if (!player.isOp()) {
                player.sendMessage(EmbellishmentCore.suffix + "당신은 권한이 없습니다.");
                return;
            }
            if (Bukkit.getPlayer(args.get(0)) == null) {
                player.sendMessage(EmbellishmentCore.suffix + "해당 플레이어는 미접속중이거나 존재하지 않는 플레이어 입니다.");
                return;
            }
            if (this.embellishmentTypeManager.getEmbellishmentType(args.get(1)) == null) {
                player.sendMessage(EmbellishmentCore.suffix + "" + args.get(1) + "치장이 존재하지 않습니다.");
                return;
            }
            if (this.embellishmentManager.getEmbellishment(player.getUniqueId(), args.get(1)) != null) {
                player.sendMessage(EmbellishmentCore.suffix + Bukkit.getPlayer(args.get(0)).getName() + "플레이어는 " + args.get(1) + "치장이 이미 존재합니다.");
                return;
            }
            this.embellishmentManager.addEmbellishment(Bukkit.getPlayer(args.get(0)).getUniqueId(), this.embellishmentTypeManager.getEmbellishmentType(args.get(1)));
            player.sendMessage(EmbellishmentCore.suffix + "해당 플레이어한테 정상적으로 치장이 추가 되었습니다.");
        } catch (Exception exception) {
            player.sendMessage(EmbellishmentCore.suffix + "명령어를 확인해주세요. ./치장 도움말");
        }
    }

    @MinecraftCommand("/치장 제거")
    public void deleteEmbellishment(Player player, CommandArgument args) {
        try {
            if (!player.isOp()) {
                player.sendMessage(EmbellishmentCore.suffix + "당신은 권한이 없습니다.");
                return;
            }
            if (Bukkit.getPlayer(args.get(0)) == null) {
                player.sendMessage(EmbellishmentCore.suffix + "해당 플레이어는 미접속중이거나 존재하지 않는 플레이어 입니다.");
                return;
            }
            if (this.embellishmentTypeManager.getEmbellishmentType(args.get(1)) == null) {
                player.sendMessage(EmbellishmentCore.suffix + "" + args.get(1) + "치장이 존재하지 않습니다.");
                return;
            }
            if (this.embellishmentManager.getEmbellishment(player.getUniqueId(), args.get(1)) == null) {
                player.sendMessage(EmbellishmentCore.suffix + Bukkit.getPlayer(args.get(0)).getName() + "플레이어는 " + args.get(1) + "치장이 존재하지 않습니다.");
                return;
            }
            this.embellishmentManager.removeEmbellishment(Bukkit.getPlayer(args.get(0)).getUniqueId(), args.get(1));
            player.sendMessage(EmbellishmentCore.suffix + "해당 플레이어한테 있는 치장이 정상적으로 제거 했습니다.");
        } catch (Exception exception) {
            player.sendMessage(EmbellishmentCore.suffix + "명령어를 확인해주세요. ./치장 도움말");
        }

    }

    @MinecraftCommand("/치장 초기화")
    public void clearEmbellishment(Player player, CommandArgument args) {
        if (!player.isOp()) {
            player.sendMessage(EmbellishmentCore.suffix + "당신은 권한이 없습니다.");
            return;
        }
        if (Bukkit.getPlayer(args.get(0)) == null) {
            player.sendMessage(EmbellishmentCore.suffix + "해당 플레이어는 미접속중이거나 존재하지 않는 플레이어 입니다.");
            return;
        }
        this.embellishmentManager.getPlayerEmbellishments().get(Bukkit.getPlayer(args.get(0)).getUniqueId()).clear();
        player.sendMessage(EmbellishmentCore.suffix + "해당 플레이어한테 있는 모든 치장을 초기화 했습니다.");


    }

    @MinecraftCommand("/치장 리로드")
    public void loadEmbellishment(Player player, CommandArgument args) {
        if (!player.isOp()) {
            player.sendMessage(EmbellishmentCore.suffix + "당신은 권한이 없습니다.");
            return;
        }
        try {
            List<IEmbellishmentType> iEmbellishmentTypes = this.embellishmentTypeConfig.getEmbellishmentTypes();
            for (IEmbellishmentType iEmbellishmentType : iEmbellishmentTypes) {
                player.sendMessage(iEmbellishmentType.getName() + " , " + iEmbellishmentType.getItem() + " , " + iEmbellishmentType.getItem().getDurability());
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

}
