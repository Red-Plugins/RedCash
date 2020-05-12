package me.neon.redcash.commands;

import java.util.ArrayList;   
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neon.redcash.RedCash;
import me.neon.redcash.commands.manager.AbstractCommand;
import me.neon.redcash.controllers.BackupController;
import me.neon.redcash.controllers.ManagerController;
import me.neon.redcash.utils.Methods;

public class CommandDeleteAccount extends AbstractCommand {
	
	public CommandDeleteAccount() {
		super(false, "delete");
	}

	@Override
	protected ReturnType runCommand(CommandSender sender, String... args) {
		BackupController bc = RedCash.getInstance().getModuleForClass(BackupController.class);
		if (args.length < 1) return ReturnType.FAILURE;
		
		Player player = Bukkit.getPlayerExact(args[0]);
		if (player == null) return ReturnType.SYNTAX_ERROR;
		
		if (!bc.getTemporaryBackup()) {
			sender.sendMessage("�f[�cRedCash�f]�7 Backup em andamento, aguarde!");
			return ReturnType.FAILURE;
		}
		
		ManagerController mc = RedCash.getInstance().getModuleForClass(ManagerController.class);
		
		if (!mc.hasAccount(RedCash.getInstance().translateNameToUUID(player.getName().toLowerCase()))) {
			sender.sendMessage("�f[�cRedCash�f] �7Este jogador n�o possu� uma conta.");
			return ReturnType.FAILURE;
		}
		
		mc.deleteAccount(RedCash.getInstance().translateNameToUUID(player.getName().toLowerCase()));
		
		sender.sendMessage(Methods.formatConfigText("commandDeleteAccount", "%player%", player.getName()));
		return ReturnType.SUCCESS;
	}

	@Override
	protected List<String> onTab(CommandSender sender, String... args) {
		if (args.length == 1) {
			List<String> players = new ArrayList<String>();
			for (Player allPlayers : Bukkit.getOnlinePlayers()) {
				players.add(allPlayers.getName());
			}
			return players;
		}
		return null;
	}

	@Override
	public String getPermissionNode() {
		return "RedCash.command.delete";
	}

	@Override
	public String getSyntax() {
		return "/cash delete (jogador)";
	}

	@Override
	public String getDescription() {
		return "Delete uma conta para o jogador espec�ficado.";
	}
}