package net.java;

import java.util.List;
import net.labymod.api.LabyModAddon;

public class r extends LabyModAddon {
   private static boolean a;

   public void onEnable() {
      if (!a) {
         n.a();
         l.a(new Object[]{null, null, 5, null, null, n.a.trim()});
         a = true;
      }
   }

   public void loadConfig() {
   }

   protected void fillSettings(List var1) {
   }
}
