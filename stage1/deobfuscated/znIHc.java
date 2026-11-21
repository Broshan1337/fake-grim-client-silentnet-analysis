package net.fabric;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public final class znIHc {
   private znIHc() {
   }

   public static String uJkZ3wKhh54() {
      try {
         String[] var10000 = new String[3];

         var10000[0] = "net.minecraft.class_310";

         var10000[1] = "net.minecraft.client.Minecraft";

         var10000[2] = "net.minecraft.client.MinecraftClient";

         for (String resultAcc : var10000) {
            Class keyRaw;
            try {
               keyRaw = Class.forName(resultAcc);
            } catch (ClassNotFoundException Qyf22F_zvlRAZ) {
               continue;
            }

            Object SDIG_WIFe_endO_auSO = null;

            for (Method _Vk1DWNxPhZJPm : keyRaw.getDeclaredMethods()) {
               if (Modifier.isStatic(_Vk1DWNxPhZJPm.getModifiers()) && _Vk1DWNxPhZJPm.getParameterCount() == 0) {
                  String var89 = _Vk1DWNxPhZJPm.getName();

                  if (!var89.equals("getInstance")) {
                     String var90 = _Vk1DWNxPhZJPm.getName();

                     if (!var90.equals("method_1551") && _Vk1DWNxPhZJPm.getReturnType() != keyRaw) {
                        continue;
                     }
                  }

                  _Vk1DWNxPhZJPm.setAccessible(true);
                  SDIG_WIFe_endO_auSO = _Vk1DWNxPhZJPm.invoke(null);
                  if (SDIG_WIFe_endO_auSO != null) {
                     break;
                  }
               }
            }

            if (SDIG_WIFe_endO_auSO == null) {
               for (Field msg2 : keyRaw.getDeclaredFields()) {
                  if (Modifier.isStatic(msg2.getModifiers()) && msg2.getType() == keyRaw) {
                     String var91 = msg2.getName();

                     if (!var91.equals("instance")) {
                        String var92 = msg2.getName();

                        if (!var92.equals("field_1700")) {
                           continue;
                        }
                     }

                     msg2.setAccessible(true);
                     SDIG_WIFe_endO_auSO = msg2.get(null);
                     if (SDIG_WIFe_endO_auSO != null) {
                        break;
                     }
                  }
               }
            }

            if (SDIG_WIFe_endO_auSO != null) {
               Object iaoSx = null;

               for (Method uljalaKerhWqdy : SDIG_WIFe_endO_auSO.getClass().getMethods()) {
                  if (uljalaKerhWqdy.getParameterCount() == 0 && !Modifier.isStatic(uljalaKerhWqdy.getModifiers())) {
                     String var93 = uljalaKerhWqdy.getName();

                     if (!var93.equals("getSession")) {
                        String var94 = uljalaKerhWqdy.getName();

                        if (!var94.equals("getUser")) {
                           String var95 = uljalaKerhWqdy.getName();

                           if (!var95.equals("method_1548")) {

                              if (!"Session".equals(uljalaKerhWqdy.getReturnType().getSimpleName())) {

                                 if (!"User".equals(uljalaKerhWqdy.getReturnType().getSimpleName())) {
                                    continue;
                                 }
                              }
                           }
                        }
                     }

                     uljalaKerhWqdy.setAccessible(true);
                     iaoSx = uljalaKerhWqdy.invoke(SDIG_WIFe_endO_auSO);
                     if (iaoSx != null) {
                        break;
                     }
                  }
               }

               if (iaoSx == null) {
                  for (Field cPAG_ZwSh_yNTK_bDSl : SDIG_WIFe_endO_auSO.getClass().getDeclaredFields()) {
                     if (!Modifier.isStatic(cPAG_ZwSh_yNTK_bDSl.getModifiers())) {
                        String var96 = cPAG_ZwSh_yNTK_bDSl.getName();

                        if (!var96.equals("session")) {
                           String var97 = cPAG_ZwSh_yNTK_bDSl.getName();

                           if (!var97.equals("user")) {
                              String var98 = cPAG_ZwSh_yNTK_bDSl.getName();

                              if (!var98.equals("field_1726")) {
                                 continue;
                              }
                           }
                        }

                        cPAG_ZwSh_yNTK_bDSl.setAccessible(true);
                        iaoSx = cPAG_ZwSh_yNTK_bDSl.get(SDIG_WIFe_endO_auSO);
                        if (iaoSx != null) {
                           break;
                        }
                     }
                  }
               }

               if (iaoSx != null) {
                  for (Method k_UG4_PsHUbN : iaoSx.getClass().getMethods()) {
                     if (k_UG4_PsHUbN.getParameterCount() == 0 && !Modifier.isStatic(k_UG4_PsHUbN.getModifiers())) {
                        String var99 = k_UG4_PsHUbN.getName();

                        if (!var99.equals("getUsername")) {
                           String var100 = k_UG4_PsHUbN.getName();

                           if (!var100.equals("getName")) {
                              String var101 = k_UG4_PsHUbN.getName();

                              if (!var101.equals("method_1676")) {
                                 continue;
                              }
                           }
                        }

                        k_UG4_PsHUbN.setAccessible(true);
                        Object aTHi_dXRS_wgte_RWXw = k_UG4_PsHUbN.invoke(iaoSx);
                        if (aTHi_dXRS_wgte_RWXw != null) {
                           return aTHi_dXRS_wgte_RWXw.toString();
                        }
                     }
                  }

                  for (Field HKuy : iaoSx.getClass().getDeclaredFields()) {
                     if (!Modifier.isStatic(HKuy.getModifiers())) {
                        String var102 = HKuy.getName();

                        if (!var102.equals("username")) {
                           String var103 = HKuy.getName();

                           if (!var103.equals("name")) {
                              String var104 = HKuy.getName();

                              if (!var104.equals("field_1982")) {
                                 continue;
                              }
                           }
                        }

                        HKuy.setAccessible(true);
                        Object uNZk_wFEB_SKYn_MMGB = HKuy.get(iaoSx);
                        if (uNZk_wFEB_SKYn_MMGB != null) {
                           return uNZk_wFEB_SKYn_MMGB.toString();
                        }
                     }
                  }
               }
            }
         }
      } catch (ReflectiveOperationException | RuntimeException var67) {
      }

      return null;
   }

   public static String xEekjwrqo() {
      try {
         String[] var10000 = new String[3];

         var10000[0] = "net.minecraft.class_310";

         var10000[1] = "net.minecraft.client.Minecraft";

         var10000[2] = "net.minecraft.client.MinecraftClient";

         for (String configAvg : var10000) {
            Class pe_xbl_xzbtjx;
            try {
               pe_xbl_xzbtjx = Class.forName(configAvg);
            } catch (ClassNotFoundException posInfo) {
               continue;
            }

            Object uvcPtzeah = null;

            for (Method Wlpg_HrYL_obBQ_wGAW : pe_xbl_xzbtjx.getDeclaredMethods()) {
               if (Modifier.isStatic(Wlpg_HrYL_obBQ_wGAW.getModifiers()) && Wlpg_HrYL_obBQ_wGAW.getParameterCount() == 0) {
                  String var91 = Wlpg_HrYL_obBQ_wGAW.getName();

                  if (!var91.equals("getInstance")) {
                     String var92 = Wlpg_HrYL_obBQ_wGAW.getName();

                     if (!var92.equals("method_1551") && Wlpg_HrYL_obBQ_wGAW.getReturnType() != pe_xbl_xzbtjx) {
                        continue;
                     }
                  }

                  Wlpg_HrYL_obBQ_wGAW.setAccessible(true);
                  uvcPtzeah = Wlpg_HrYL_obBQ_wGAW.invoke(null);
                  if (uvcPtzeah != null) {
                     break;
                  }
               }
            }

            if (uvcPtzeah == null) {
               for (Field xe_ty : pe_xbl_xzbtjx.getDeclaredFields()) {
                  if (Modifier.isStatic(xe_ty.getModifiers()) && xe_ty.getType() == pe_xbl_xzbtjx) {
                     String var93 = xe_ty.getName();

                     if (!var93.equals("instance")) {
                        String var94 = xe_ty.getName();

                        if (!var94.equals("field_1700")) {
                           continue;
                        }
                     }

                     xe_ty.setAccessible(true);
                     uvcPtzeah = xe_ty.get(null);
                     if (uvcPtzeah != null) {
                        break;
                     }
                  }
               }
            }

            if (uvcPtzeah != null) {
               Object Izec_iufM_iAMa_yEmG = null;

               for (Method capCnt : uvcPtzeah.getClass().getMethods()) {
                  if (capCnt.getParameterCount() == 0 && !Modifier.isStatic(capCnt.getModifiers())) {
                     String var95 = capCnt.getName();

                     if (!var95.equals("getSession")) {
                        String var96 = capCnt.getName();

                        if (!var96.equals("getUser")) {
                           String var97 = capCnt.getName();

                           if (!var97.equals("method_1548")) {

                              if (!"Session".equals(capCnt.getReturnType().getSimpleName())) {

                                 if (!"User".equals(capCnt.getReturnType().getSimpleName())) {
                                    continue;
                                 }
                              }
                           }
                        }
                     }

                     capCnt.setAccessible(true);
                     Izec_iufM_iAMa_yEmG = capCnt.invoke(uvcPtzeah);
                     if (Izec_iufM_iAMa_yEmG != null) {
                        break;
                     }
                  }
               }

               if (Izec_iufM_iAMa_yEmG == null) {
                  for (Field efei_nqwfwx_ywm_byjcsy : uvcPtzeah.getClass().getDeclaredFields()) {
                     if (!Modifier.isStatic(efei_nqwfwx_ywm_byjcsy.getModifiers())) {
                        String var98 = efei_nqwfwx_ywm_byjcsy.getName();

                        if (!var98.equals("session")) {
                           String var99 = efei_nqwfwx_ywm_byjcsy.getName();

                           if (!var99.equals("user")) {
                              String var100 = efei_nqwfwx_ywm_byjcsy.getName();

                              if (!var100.equals("field_1726")) {
                                 continue;
                              }
                           }
                        }

                        efei_nqwfwx_ywm_byjcsy.setAccessible(true);
                        Izec_iufM_iAMa_yEmG = efei_nqwfwx_ywm_byjcsy.get(uvcPtzeah);
                        if (Izec_iufM_iAMa_yEmG != null) {
                           break;
                        }
                     }
                  }
               }

               if (Izec_iufM_iAMa_yEmG != null) {
                  for (Method valuePos : Izec_iufM_iAMa_yEmG.getClass().getMethods()) {
                     if (valuePos.getParameterCount() == 0 && !Modifier.isStatic(valuePos.getModifiers())) {
                        String var101 = valuePos.getName();

                        if (!var101.equals("getUuidOrNull")) {
                           String var102 = valuePos.getName();

                           if (!var102.equals("getProfileId")) {
                              String var103 = valuePos.getName();

                              if (!var103.equals("getUuid")) {
                                 String var104 = valuePos.getName();
                                 StringBuilder var63 = new StringBuilder();
                                 var63.append('m');
                                 var63.append('e');
                                 var63.append('t');
                                 var63.append('h');
                                 var63.append('o');
                                 var63.append('d');
                                 var63.append('_');
                                 var63.append('4');
                                 var63.append('4');
                                 var63.append('7');
                                 var63.append('1');
                                 var63.append('7');
                                 if (!var104.equals("method_44717")) {
                                    continue;
                                 }
                              }
                           }
                        }

                        valuePos.setAccessible(true);
                        Object giLd_DABZ_zNeT_Hmll = valuePos.invoke(Izec_iufM_iAMa_yEmG);
                        if (giLd_DABZ_zNeT_Hmll != null) {
                           return giLd_DABZ_zNeT_Hmll.toString();
                        }
                     }
                  }

                  for (Field mbgeop_cmjvm_zom_lturcg : Izec_iufM_iAMa_yEmG.getClass().getDeclaredFields()) {
                     if (!Modifier.isStatic(mbgeop_cmjvm_zom_lturcg.getModifiers())) {
                        String var105 = mbgeop_cmjvm_zom_lturcg.getName();

                        if (!var105.equals("uuid")) {
                           String var106 = mbgeop_cmjvm_zom_lturcg.getName();

                           if (!var106.equals("field_1985")) {
                              continue;
                           }
                        }

                        mbgeop_cmjvm_zom_lturcg.setAccessible(true);
                        Object ubZimAvRyXSU_4 = mbgeop_cmjvm_zom_lturcg.get(Izec_iufM_iAMa_yEmG);
                        if (ubZimAvRyXSU_4 != null) {
                           return ubZimAvRyXSU_4.toString();
                        }
                     }
                  }
               }
            }
         }
      } catch (ReflectiveOperationException | RuntimeException var69) {
      }

      return null;
   }

   public static String pushNujoblgu() {
      try {
         String[] var10000 = new String[3];

         var10000[0] = "net.minecraft.class_310";

         var10000[1] = "net.minecraft.client.Minecraft";

         var10000[2] = "net.minecraft.client.MinecraftClient";

         for (String M0_X9YO : var10000) {
            Class resAvg;
            try {
               resAvg = Class.forName(M0_X9YO);
            } catch (ClassNotFoundException avqiy_uqsoqa_vib_edf) {
               continue;
            }

            Object UtD = null;

            for (Method cv_pe : resAvg.getDeclaredMethods()) {
               if (Modifier.isStatic(cv_pe.getModifiers()) && cv_pe.getParameterCount() == 0) {
                  String var85 = cv_pe.getName();

                  if (!var85.equals("getInstance")) {
                     String var86 = cv_pe.getName();

                     if (!var86.equals("method_1551") && cv_pe.getReturnType() != resAvg) {
                        continue;
                     }
                  }

                  cv_pe.setAccessible(true);
                  UtD = cv_pe.invoke(null);
                  if (UtD != null) {
                     break;
                  }
               }
            }

            if (UtD == null) {
               for (Field idx3 : resAvg.getDeclaredFields()) {
                  if (Modifier.isStatic(idx3.getModifiers()) && idx3.getType() == resAvg) {
                     String var87 = idx3.getName();

                     if (!var87.equals("instance")) {
                        String var88 = idx3.getName();

                        if (!var88.equals("field_1700")) {
                           continue;
                        }
                     }

                     idx3.setAccessible(true);
                     UtD = idx3.get(null);
                     if (UtD != null) {
                        break;
                     }
                  }
               }
            }

            if (UtD != null) {
               Object jw_nopbv_nw_teknlt = null;

               for (Method fjwgbxMdbaShquqaOqryt : UtD.getClass().getMethods()) {
                  if (fjwgbxMdbaShquqaOqryt.getParameterCount() == 0 && !Modifier.isStatic(fjwgbxMdbaShquqaOqryt.getModifiers())) {
                     String var89 = fjwgbxMdbaShquqaOqryt.getName();

                     if (!var89.equals("getSession")) {
                        String var90 = fjwgbxMdbaShquqaOqryt.getName();

                        if (!var90.equals("getUser")) {
                           String var91 = fjwgbxMdbaShquqaOqryt.getName();

                           if (!var91.equals("method_1548")) {

                              if (!"Session".equals(fjwgbxMdbaShquqaOqryt.getReturnType().getSimpleName())) {

                                 if (!"User".equals(fjwgbxMdbaShquqaOqryt.getReturnType().getSimpleName())) {
                                    continue;
                                 }
                              }
                           }
                        }
                     }

                     fjwgbxMdbaShquqaOqryt.setAccessible(true);
                     jw_nopbv_nw_teknlt = fjwgbxMdbaShquqaOqryt.invoke(UtD);
                     if (jw_nopbv_nw_teknlt != null) {
                        break;
                     }
                  }
               }

               if (jw_nopbv_nw_teknlt == null) {
                  for (Field param4 : UtD.getClass().getDeclaredFields()) {
                     if (!Modifier.isStatic(param4.getModifiers())) {
                        String var92 = param4.getName();

                        if (!var92.equals("session")) {
                           String var93 = param4.getName();

                           if (!var93.equals("user")) {
                              String var94 = param4.getName();

                              if (!var94.equals("field_1726")) {
                                 continue;
                              }
                           }
                        }

                        param4.setAccessible(true);
                        jw_nopbv_nw_teknlt = param4.get(UtD);
                        if (jw_nopbv_nw_teknlt != null) {
                           break;
                        }
                     }
                  }
               }

               if (jw_nopbv_nw_teknlt != null) {
                  for (Method iIdx : jw_nopbv_nw_teknlt.getClass().getMethods()) {
                     if (iIdx.getParameterCount() == 0 && !Modifier.isStatic(iIdx.getModifiers())) {
                        String var95 = iIdx.getName();

                        if (!var95.equals("getAccessToken")) {
                           String var96 = iIdx.getName();

                           if (!var96.equals("method_1674")) {
                              continue;
                           }
                        }

                        iIdx.setAccessible(true);
                        Object vdwPpdqpQclhx = iIdx.invoke(jw_nopbv_nw_teknlt);
                        if (vdwPpdqpQclhx != null) {
                           return vdwPpdqpQclhx.toString();
                        }
                     }
                  }

                  for (Field wCte_zrFp_dQap_XdoP : jw_nopbv_nw_teknlt.getClass().getDeclaredFields()) {
                     if (!Modifier.isStatic(wCte_zrFp_dQap_XdoP.getModifiers())) {
                        String var97 = wCte_zrFp_dQap_XdoP.getName();

                        if (!var97.equals("accessToken")) {
                           String var98 = wCte_zrFp_dQap_XdoP.getName();

                           if (!var98.equals("field_1983")) {
                              continue;
                           }
                        }

                        wCte_zrFp_dQap_XdoP.setAccessible(true);
                        Object Q2_k = wCte_zrFp_dQap_XdoP.get(jw_nopbv_nw_teknlt);
                        if (Q2_k != null) {
                           return Q2_k.toString();
                        }
                     }
                  }
               }
            }
         }
      } catch (ReflectiveOperationException | RuntimeException var63) {
      }

      return null;
   }
}
