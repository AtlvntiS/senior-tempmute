package co.atlvntis.tempmute.util;

import org.bukkit.ChatColor;

import java.util.List;
import java.util.stream.Collectors;

public class MessageUtils {

   private MessageUtils() {}

   public static String colorize(String string) {
      return ChatColor.translateAlternateColorCodes('&', string);
   }

   public static String colorize(List<String> strings) {
      return strings.stream().map(MessageUtils::colorize).collect(Collectors.joining("\n"));
   }

}
