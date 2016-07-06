/*
 * Copyright (C) 2016 AlternaCraft
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.jlh.pvptitles.Commands;

import es.jlh.pvptitles.Backend.DatabaseManager;
import es.jlh.pvptitles.Files.LangFile;
import es.jlh.pvptitles.Main.Handlers.DBHandler;
import es.jlh.pvptitles.Main.Manager;
import es.jlh.pvptitles.Main.PvpTitles;
import static es.jlh.pvptitles.Main.PvpTitles.PLUGIN;
import es.jlh.pvptitles.Misc.Localizer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DBCommand implements CommandExecutor {

    private PvpTitles pvpTitles = null;

    public DBCommand(PvpTitles pvpTitles) {
        this.pvpTitles = pvpTitles;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
        LangFile.LangType messages = (sender instanceof Player) ? Localizer.getLocale((Player) sender) : Manager.messages;

        if (args.length == 0) {
            sender.sendMessage(PLUGIN + LangFile.COMMAND_ARGUMENTS.getText(messages));
            return false;
        }

        DatabaseManager dm = pvpTitles.manager.getDbh().getDm();

        String filename;

        switch (args[0]) {
            case "export":
                if (args.length > 1) {
                    filename = args[1] + ((DBHandler.tipo == DBHandler.DBTYPE.EBEAN) ? ".sql" : ".json");
                } else {
                    filename = pvpTitles.manager.dbh.getDm().getDefaultFExport();
                }
                dm.DBExport(filename);
                sender.sendMessage(PLUGIN + ChatColor.YELLOW + "Exported correctly");
                break;
            case "import":
                if (args.length > 1) {
                    filename = args[1] + ((DBHandler.tipo == DBHandler.DBTYPE.EBEAN) ? ".json" : ".sql");
                } else {
                    filename = pvpTitles.manager.dbh.getDm().getDefaultFImport();
                }
                if (dm.DBImport(filename)) {
                    sender.sendMessage(PLUGIN + ChatColor.YELLOW + "Imported correctly");
                } else {
                    sender.sendMessage(PLUGIN + ChatColor.RED + "File '"
                            + filename + "' not found...");
                }
                break;
            default:
                return false;
        }

        return true;
    }
}
