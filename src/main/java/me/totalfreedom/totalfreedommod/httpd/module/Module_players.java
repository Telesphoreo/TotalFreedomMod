package me.totalfreedom.totalfreedommod.httpd.module;

import me.totalfreedom.totalfreedommod.TotalFreedomMod;
import me.totalfreedom.totalfreedommod.admin.Admin;
import me.totalfreedom.totalfreedommod.config.ConfigEntry;
import me.totalfreedom.totalfreedommod.httpd.NanoHTTPD;
import me.totalfreedom.totalfreedommod.masterbuilder.MasterBuilder;
import me.totalfreedom.totalfreedommod.util.FUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Module_players extends HTTPDModule
{
    public Module_players(TotalFreedomMod plugin, NanoHTTPD.HTTPSession session)
    {
        super(plugin, session);
    }

    @Override
    @SuppressWarnings("unchecked")
    public NanoHTTPD.Response getResponse()
    {
        final JSONObject responseObject = new JSONObject();

        final JSONArray players = new JSONArray();
        final JSONArray onlineadmins = new JSONArray();
        final JSONArray masterbuilders = new JSONArray();
        final JSONArray superadmins = new JSONArray();
        final JSONArray telnetadmins = new JSONArray();
        final JSONArray senioradmins = new JSONArray();
        final JSONArray developers = new JSONArray();
        final JSONArray executives = new JSONArray();

        // All online players
        for (Player player : Bukkit.getOnlinePlayers())
        {
            players.add(player.getName());
            if (plugin.al.isAdmin(player) && !plugin.al.isAdminImpostor(player))
            {
                onlineadmins.add(player.getName());
            }
        }

        // Admins
        for (Admin admin : plugin.al.getActiveAdmins())
        {
            final String username = admin.getName();

            switch (admin.getRank())
            {
                case SUPER_ADMIN:
                    superadmins.add(username);
                    break;
                case TELNET_ADMIN:
                    telnetadmins.add(username);
                    break;
                case SENIOR_ADMIN:
                    senioradmins.add(username);
                    break;
            }
        }

        // Master Builders
        for (MasterBuilder masterBuilder : plugin.mbl.getAllMasterBuilders().values())
        {
            masterbuilders.add(masterBuilder.getName());
        }

        // Developers
        developers.addAll(FUtil.DEVELOPERS);

        // Executives
        executives.addAll(ConfigEntry.SERVER_EXECUTIVES.getList());

        responseObject.put("players", players);
        responseObject.put("masterbuilders", masterbuilders);
        responseObject.put("superadmins", superadmins);
        responseObject.put("telnetadmins", telnetadmins);
        responseObject.put("senioradmins", senioradmins);
        responseObject.put("developers", developers);
        responseObject.put("executives", developers);

        final NanoHTTPD.Response response = new NanoHTTPD.Response(NanoHTTPD.Response.Status.OK, NanoHTTPD.MIME_JSON, responseObject.toString());
        response.addHeader("Access-Control-Allow-Origin", "*");
        return response;
    }
}