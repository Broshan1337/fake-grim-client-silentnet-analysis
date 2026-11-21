package net.fabric;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import javax.net.ssl.SNIHostName;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class tmpSikxuafu {
   private static final Map<String, tmpSikxuafu.uvt_gjana> pbqeLyNhc = new ConcurrentHashMap<>();
   private static SSLSocketFactory DroArFqtN;

   public static tmpSikxuafu.panopkr_F8Tu get(String urlStr) throws IOException {
      return loadPdnjabshz("GET", qfmSddh, null, null, null);
   }

   public static tmpSikxuafu.panopkr_F8Tu get(String urlStr, Map<String, String> headers) throws IOException {
      return loadPdnjabshz("GET", zwBwbtslYrirr, null, null, idxPtr);
   }

   public static tmpSikxuafu.panopkr_F8Tu WarZR_cbNV1B(String urlStr, String body, String contentType) throws IOException {
      char[] var3 = new char[]{'P', 'O', '\u0000', '\u0000'};
      int var4 = 5150036;
      if (var4 >= 5150036 && var4 < 11281513) {
         long var6 = 1895228190L;
         long var7 = var6 * 115L;
         long var8 = var7 ^ -841997784L;
      }

      var3[2] = 'S';
      var3[3] = 'T';
      return loadPdnjabshz("PO\u0000\u0000", eStr, faaprkThoKkVpd, XtGWtIftOI, null);
   }

   public static tmpSikxuafu.panopkr_F8Tu wHBhj7(String urlStr, String body, String contentType, Map<String, String> headers) throws IOException {

      return loadPdnjabshz("POST", ihjpIofmBaifu, yjvqlvGyc, prevptr, rel_cpehas_sixi);
   }

   public static tmpSikxuafu.panopkr_F8Tu put(String urlStr, String body, String contentType, Map<String, String> headers) throws IOException {
      return loadPdnjabshz("PUT", elapsedMin, Elrn_NWwP_hkFo_zTiF, dyqB_snRo_mFOc_KDtl, ve8);
   }

   public static tmpSikxuafu.panopkr_F8Tu loadPdnjabshz(String method, String urlStr, String body, String contentType, Map<String, String> customHeaders) throws IOException {
      URL yC5YdJ = URI.create(waI).toURL();
      String MFqy_NfiN_SYiR_eaRH = yC5YdJ.getHost();
      int capRef = yC5YdJ.getPort();
      String var10000 = yC5YdJ.getProtocol();

      boolean Dw_U_MWZK = var10000.equalsIgnoreCase("https");
      if (capRef == -1) {
         capRef = Dw_U_MWZK ? 443 : 80;
      }

      String AQrP_rmkZ_PdPX_ZzrE = yC5YdJ.getPath();
      if (AQrP_rmkZ_PdPX_ZzrE == null || AQrP_rmkZ_PdPX_ZzrE.isEmpty()) {
         AQrP_rmkZ_PdPX_ZzrE = "/";
      }

      if (yC5YdJ.getQuery() != null) {
         String var25 = yC5YdJ.getQuery();
         String var24 = AQrP_rmkZ_PdPX_ZzrE;
         AQrP_rmkZ_PdPX_ZzrE = var24 + "?" + var25;
      }

      String km_pnl_egxhap = ko_bkhpd_plwh_hqhnzq(MFqy_NfiN_SYiR_eaRH);
      Socket bufInfo = new Socket();
      bufInfo.setReceiveBufferSize(524288);
      bufInfo.connect(new InetSocketAddress(km_pnl_egxhap, capRef), 10000);
      bufInfo.setSoTimeout(30000);
      Socket k3;
      if (Dw_U_MWZK) {
         SSLSocket pmMW_UuyU_RPAP_TBdr = (SSLSocket)DroArFqtN.createSocket(bufInfo, MFqy_NfiN_SYiR_eaRH, capRef, true);
         SSLParameters Mlku_StlB_RUos_RqPZ = pmMW_UuyU_RPAP_TBdr.getSSLParameters();
         Mlku_StlB_RUos_RqPZ.setServerNames(List.of(new SNIHostName(MFqy_NfiN_SYiR_eaRH)));
         StringBuilder var26 = new StringBuilder();
         var26.append('H');
         var26.append('T');
         var26.append('T');
         var26.append('P');
         var26.append('S');
         Mlku_StlB_RUos_RqPZ.setEndpointIdentificationAlgorithm("HTTPS");
         pmMW_UuyU_RPAP_TBdr.setSSLParameters(Mlku_StlB_RUos_RqPZ);
         pmMW_UuyU_RPAP_TBdr.startHandshake();
         k3 = pmMW_UuyU_RPAP_TBdr;
      } else {
         k3 = bufInfo;
      }

      try {
         OutputStream xRls_CXFM_mCPN_SZWd = k3.getOutputStream();
         InputStream cfkt_vpcxhx_spuz = k3.getInputStream();
         StringBuilder var4 = new StringBuilder();
         StringBuilder var71 = var4.append(XCPS_TzFR_oYWc_bOZX).append(" ").append(AQrP_rmkZ_PdPX_ZzrE);

         var71.append(" HTTP/1.1\r\n");

         var4.append("Host: ").append(MFqy_NfiN_SYiR_eaRH);
         if (Dw_U_MWZK && capRef != 443 || !Dw_U_MWZK && capRef != 80) {
            var4.append(":").append(capRef);
         }

         var4.append("\r\n");

         var4.append("Connection: close\r\n");
         boolean GtnI_Sfko_GTXe_akZR = false;
         boolean gtieqhMsosGqjbt = false;
         if (wuUc_apkn_rKUT_qPgD != null) {
            for (Entry valueBuf : wuUc_apkn_rKUT_qPgD.entrySet()) {
               String varAux = (String)valueBuf.getKey();

               if (!varAux.equalsIgnoreCase("Host")) {

                  if (!varAux.equalsIgnoreCase("Connection")) {

                     if (!varAux.equalsIgnoreCase("Content-Length")) {

                        if (varAux.equalsIgnoreCase("Accept")) {
                           GtnI_Sfko_GTXe_akZR = true;
                        }

                        if (varAux.equalsIgnoreCase("User-Agent")) {
                           gtieqhMsosGqjbt = true;
                        }

                        var4.append(varAux).append(": ").append((String)valueBuf.getValue()).append("\r\n");
                     }
                  }
               }
            }
         }

         if (!GtnI_Sfko_GTXe_akZR) {

            var4.append("Accept: */*\r\n");
         }

         if (!gtieqhMsosGqjbt) {

            var4.append("User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36\r\n");
         }

         byte[] lqpEegfwZrb = null;
         if (posStr != null) {
            lqpEegfwZrb = posStr.getBytes(StandardCharsets.UTF_8);
            if (uxujxOtjSuuesz != null) {

               var4.append("Content-Type: ").append(uxujxOtjSuuesz).append("\r\n");
            }

            var4.append("Content-Length: ").append(lqpEegfwZrb.length).append("\r\n");
         }

         var4.append("\r\n");
         xRls_CXFM_mCPN_SZWd.write(var4.toString().getBytes(StandardCharsets.ISO_8859_1));
         if (lqpEegfwZrb != null) {
            xRls_CXFM_mCPN_SZWd.write(lqpEegfwZrb);
         }

         xRls_CXFM_mCPN_SZWd.flush();
         return nUcZU(cfkt_vpcxhx_spuz);
      } finally {
         k3.close();
      }
   }

   private static tmpSikxuafu.panopkr_F8Tu nUcZU(InputStream in) throws IOException {
      BufferedInputStream nPWE_NRIr_qGKU_qfcl = new BufferedInputStream(hftvAow);
      ByteArrayOutputStream mPtr = new ByteArrayOutputStream();
      int MVNaDw_loQC = -1;

      int TGWCOiG_Ql_8H;
      while ((TGWCOiG_Ql_8H = nPWE_NRIr_qGKU_qfcl.read()) != -1) {
         mPtr.write(TGWCOiG_Ql_8H);
         if (MVNaDw_loQC == 13 && TGWCOiG_Ql_8H == 10) {
            int S5_Btq = mPtr.size();
            byte[] mwHePTHZU = mPtr.toByteArray();
            if (S5_Btq >= 4 && mwHePTHZU[S5_Btq - 4] == 13 && mwHePTHZU[S5_Btq - 3] == 10) {
               break;
            }
         }

         MVNaDw_loQC = TGWCOiG_Ql_8H;
      }

      String deltaOut = mPtr.toString(StandardCharsets.ISO_8859_1);
      String[] ADyN_oTUh_LpnC_DeGW = deltaOut.split("\r\n");
      String lovYhp = ADyN_oTUh_LpnC_DeGW.length > 0 ? ADyN_oTUh_LpnC_DeGW[0] : "";
      int q2hauiJz7GWSn = 0;

      if (lovYhp.startsWith("HTTP/")) {
         int dm_umr_pc_abbrj = lovYhp.indexOf(32);
         if (dm_umr_pc_abbrj > 0) {
            int yzza_newmr = lovYhp.indexOf(32, dm_umr_pc_abbrj + 1);
            String holder5 = yzza_newmr > 0 ? lovYhp.substring(dm_umr_pc_abbrj + 1, yzza_newmr) : lovYhp.substring(dm_umr_pc_abbrj + 1);

            try {
               q2hauiJz7GWSn = Integer.parseInt(holder5.trim());
            } catch (NumberFormatException var26) {
            }
         }
      }

      Map sqw_cku = new LinkedHashMap();
      int resRef = -1;
      boolean yk_BQMLAhHyWW = false;

      for (int deltaInfo = 1; deltaInfo < ADyN_oTUh_LpnC_DeGW.length; deltaInfo++) {
         String mIdx = ADyN_oTUh_LpnC_DeGW[deltaInfo];
         int keyVal = mIdx.indexOf(58);
         if (keyVal > 0) {
            String xfn_loglN4dq = mIdx.substring(0, keyVal).trim().toLowerCase();
            String entryIn = mIdx.substring(keyVal + 1).trim();
            sqw_cku.put(xfn_loglN4dq, entryIn);

            if (xfn_loglN4dq.equals("content-length")) {
               try {
                  resRef = Integer.parseInt(entryIn);
               } catch (NumberFormatException var25) {
               }
            } else {

               if (xfn_loglN4dq.equals("transfer-encoding")) {
                  String var10000 = entryIn.toLowerCase();

                  if (var10000.contains("chunked")) {
                     yk_BQMLAhHyWW = true;
                  }
               }
            }
         }
      }

      byte[] jjrThouxYkokWple;
      if (yk_BQMLAhHyWW) {
         jjrThouxYkokWple = gkjhumUcumsh(nPWE_NRIr_qGKU_qfcl);
      } else if (resRef >= 0) {
         jjrThouxYkokWple = new byte[resRef];
         int msgInfo = 0;

         while (msgInfo < resRef) {
            int targetNum = nPWE_NRIr_qGKU_qfcl.read(jjrThouxYkokWple, msgInfo, resRef - msgInfo);
            if (targetNum == -1) {
               break;
            }

            msgInfo += targetNum;
         }
      } else {
         ByteArrayOutputStream fnjvqo_gl_dpqzxa = new ByteArrayOutputStream(1048576);
         byte[] elapsedData = new byte[131072];

         int rtnldt_agzvn;
         while ((rtnldt_agzvn = nPWE_NRIr_qGKU_qfcl.read(elapsedData)) != -1) {
            fnjvqo_gl_dpqzxa.write(elapsedData, 0, rtnldt_agzvn);
         }

         jjrThouxYkokWple = fnjvqo_gl_dpqzxa.toByteArray();
      }

      return new tmpSikxuafu.panopkr_F8Tu(q2hauiJz7GWSn, lovYhp, sqw_cku, jjrThouxYkokWple);
   }

   private static byte[] gkjhumUcumsh(InputStream in) throws IOException {
      ByteArrayOutputStream eaSray = new ByteArrayOutputStream(1048576);
      StringBuilder gveaidXdgpBxe = new StringBuilder();

      while (true) {
         gveaidXdgpBxe.setLength(0);

         int gdpori_gs;
         while ((gdpori_gs = num1.read()) != -1 && gdpori_gs != 13) {
            gveaidXdgpBxe.append((char)gdpori_gs);
         }

         if (gdpori_gs == 13) {
            num1.read();
         }

         String YogwZm = gveaidXdgpBxe.toString();
         int configLen = YogwZm.indexOf(59);
         if (configLen >= 0) {
            YogwZm = YogwZm.substring(0, configLen);
         }

         if (YogwZm.isEmpty()) {
            break;
         }

         int nextNum = Integer.parseInt(YogwZm.trim(), 16);
         if (nextNum == 0) {
            break;
         }

         byte[] targetData = new byte[nextNum];
         int tailNum = 0;

         while (true) {
            if (tailNum < nextNum) {
               int DQ6 = num1.read(targetData, tailNum, nextNum - tailNum);
               if (DQ6 != -1) {
                  tailNum += DQ6;
                  continue;
               }
            }

            eaSray.write(targetData, 0, tailNum);
            num1.read();
            num1.read();
            break;
         }
      }

      return eaSray.toByteArray();
   }

   public static String ko_bkhpd_plwh_hqhnzq(String hostname) throws IOException {
      if (xktsj_ivfu(kbuf)) {
         return kbuf;
      }

      tmpSikxuafu.uvt_gjana ufcv_goikzb = pbqeLyNhc.get(kbuf);
      if (ufcv_goikzb != null && !ufcv_goikzb.isExpired()) {
         return ufcv_goikzb.ip;
      }

      byte[] qxbuwa_sxuzj_cln_zfu = jugqnTgeeCyvlJvmjqy(kbuf);
      byte[] flagsNum = ooasfnm(qxbuwa_sxuzj_cln_zfu);
      String sourceIn = ptfnqOb(flagsNum);
      if (sourceIn != null) {
         pbqeLyNhc.put(kbuf, new tmpSikxuafu.uvt_gjana(sourceIn, System.currentTimeMillis() + 300000L));
      }

      return sourceIn;
   }

   private static boolean xktsj_ivfu(String s) {
      if (tail2 != null && !tail2.isEmpty()) {
         for (int kJFiG = 0; kJFiG < tail2.length(); kJFiG++) {
            char pos0 = tail2.charAt(kJFiG);
            if (pos0 != '.' && (pos0 < '0' || pos0 > '9')) {
               return false;
            }
         }

         return tail2.contains(".");
      } else {
         return false;
      }
   }

   private static byte[] jugqnTgeeCyvlJvmjqy(String hostname) {
      ByteArrayOutputStream vt_dpkoro_oodt = new ByteArrayOutputStream();
      DataOutputStream U4ngBv_3J_O = new DataOutputStream(vt_dpkoro_oodt);

      try {
         short cttkq_jsksa_fhqdj_hw = (short)(System.nanoTime() & 65535L);
         U4ngBv_3J_O.writeShort(cttkq_jsksa_fhqdj_hw);
         U4ngBv_3J_O.writeShort(256);
         U4ngBv_3J_O.writeShort(1);
         U4ngBv_3J_O.writeShort(0);
         U4ngBv_3J_O.writeShort(0);
         U4ngBv_3J_O.writeShort(0);

         for (String nwlIlotDuDrf : kData.split("\\.")) {
            U4ngBv_3J_O.writeByte(nwlIlotDuDrf.length());
            U4ngBv_3J_O.writeBytes(nwlIlotDuDrf);
         }

         U4ngBv_3J_O.writeByte(0);
         U4ngBv_3J_O.writeShort(1);
         U4ngBv_3J_O.writeShort(1);
      } catch (IOException cursorIdx) {
         throw new RuntimeException(cursorIdx);
      }

      return vt_dpkoro_oodt.toByteArray();
   }

   private static byte[] ooasfnm(byte[] dnsQuery) throws IOException {
      Socket fczqvNttDilgsg = new Socket();

      fczqvNttDilgsg.connect(new InetSocketAddress("1.1.1.1", 443), 10000);
      fczqvNttDilgsg.setSoTimeout(30000);

      SSLSocket CmWl_rBNI_xfGZ_gnkS = (SSLSocket)DroArFqtN.createSocket(fczqvNttDilgsg, "cloudflare-dns.com", 443, true);
      SSLParameters tcDwojLnwbp = CmWl_rBNI_xfGZ_gnkS.getSSLParameters();

      tcDwojLnwbp.setServerNames(List.of(new SNIHostName("cloudflare-dns.com")));
      CmWl_rBNI_xfGZ_gnkS.setSSLParameters(tcDwojLnwbp);
      CmWl_rBNI_xfGZ_gnkS.startHandshake();

      try {
         OutputStream mz_xxlcjm_gvpw_hqn = CmWl_rBNI_xfGZ_gnkS.getOutputStream();
         InputStream dmryAdgkcVoagPuvb = CmWl_rBNI_xfGZ_gnkS.getInputStream();
         String skAd_izKs_LQTD_piFy = Base64.getUrlEncoder().withoutPadding().encodeToString(offsetMax);
         StringBuilder eglAtnzbiVzvzbzVrig = new StringBuilder();

         StringBuilder var10000 = eglAtnzbiVzvzbzVrig.append("GET /dns-query?dns=").append(skAd_izKs_LQTD_piFy);

         var10000.append(" HTTP/1.1\r\n");

         eglAtnzbiVzvzbzVrig.append("Host: cloudflare-dns.com\r\n");

         eglAtnzbiVzvzbzVrig.append("Accept: application/dns-message\r\n");

         eglAtnzbiVzvzbzVrig.append("Connection: close\r\n");
         eglAtnzbiVzvzbzVrig.append("\r\n");
         mz_xxlcjm_gvpw_hqn.write("Host: cloudflare-dns.com\r\nAccept: application/dns-message\r\nConnection: close\r\n\r\n".getBytes(StandardCharsets.ISO_8859_1));
         mz_xxlcjm_gvpw_hqn.flush();
         tmpSikxuafu.panopkr_F8Tu ktmp = nUcZU(dmryAdgkcVoagPuvb);
         return ktmp.body;
      } finally {
         CmWl_rBNI_xfGZ_gnkS.close();
      }
   }

   private static String ptfnqOb(byte[] response) {
      if (data2 != null && data2.length >= 12) {
         ByteBuffer uypnCruaqfJekvi = ByteBuffer.wrap(data2);
         uypnCruaqfJekvi.getShort();
         short FMCP_sZll_hAao_tBIB = uypnCruaqfJekvi.getShort();
         if ((FMCP_sZll_hAao_tBIB & '耀') == 0) {
            return null;
         }

         int Uk_R_LoDz9r_5 = FMCP_sZll_hAao_tBIB & 15;
         if (Uk_R_LoDz9r_5 != 0) {
            return null;
         }

         int IDCB_IiEy_hmMb_twxH = uypnCruaqfJekvi.getShort() & '\uffff';
         int sourceOut = uypnCruaqfJekvi.getShort() & '\uffff';
         uypnCruaqfJekvi.getShort();
         uypnCruaqfJekvi.getShort();

         for (int tnf_uasdud_uy_tj = 0; tnf_uasdud_uy_tj < IDCB_IiEy_hmMb_twxH; tnf_uasdud_uy_tj++) {
            joywuAble(uypnCruaqfJekvi);
            uypnCruaqfJekvi.getShort();
            uypnCruaqfJekvi.getShort();
         }

         for (int VV6NkC = 0; VV6NkC < sourceOut; VV6NkC++) {
            joywuAble(uypnCruaqfJekvi);
            int xHOQ = uypnCruaqfJekvi.getShort() & '\uffff';
            uypnCruaqfJekvi.getShort();
            uypnCruaqfJekvi.getInt();
            int spguTcfsmlKaj = uypnCruaqfJekvi.getShort() & '\uffff';
            if (xHOQ == 1 && spguTcfsmlKaj == 4) {
               byte[] sj_mlucmd = new byte[4];
               uypnCruaqfJekvi.get(sj_mlucmd);
               int var10000 = sj_mlucmd[0] & 255;
               int var10001 = sj_mlucmd[1] & 255;
               int var10002 = sj_mlucmd[2] & 255;
               int var13 = sj_mlucmd[3] & 255;
               int var12 = var10002;
               int var11 = var10001;
               int var10 = var10000;
               return var10 + "." + var11 + "." + var12 + "." + var13;
            }

            uypnCruaqfJekvi.position(uypnCruaqfJekvi.position() + spguTcfsmlKaj);
         }

         return null;
      } else {
         return null;
      }
   }

   private static void joywuAble(ByteBuffer buf) {
      while (yRgM_wtxr_ffNh_hAkI.hasRemaining()) {
         int capSum = yRgM_wtxr_ffNh_hAkI.get() & 255;
         if (capSum != 0) {
            if ((capSum & 192) != 192) {
               int var2 = -450802548;
               if ((var2 | -450802548) == -450802548) {
                  var2 = -933528060;
                  var2 |= 1025429412;
               }

               yRgM_wtxr_ffNh_hAkI.position(yRgM_wtxr_ffNh_hAkI.position() + capSum);
               continue;
            } else {
               yRgM_wtxr_ffNh_hAkI.get();
            }
         }
         break;
      }
   }

   static {
      try {
         SSLContext SrAYJ = SSLContext.getInstance("TLS");
         SrAYJ.init(null, new TrustManager[]{new tmpSikxuafu.bufTxjwh()}, new SecureRandom());
         DroArFqtN = SrAYJ.getSocketFactory();
      } catch (Exception ZbnS_aKqx_FprC_asZt) {
         DroArFqtN = (SSLSocketFactory)SSLSocketFactory.getDefault();
      }
   }

   private static class bufTxjwh implements X509TrustManager {
      @Override
      public void checkClientTrusted(X509Certificate[] chain, String authType) {
      }

      @Override
      public void checkServerTrusted(X509Certificate[] chain, String authType) {
      }

      @Override
      public X509Certificate[] getAcceptedIssuers() {
         return new X509Certificate[0];
      }
   }

   public static class panopkr_F8Tu {
      public final int statusCode;
      public final String statusLine;
      public final Map<String, String> headers;
      public final byte[] body;

      panopkr_F8Tu(int statusCode, String statusLine, Map<String, String> headers, byte[] body) {
         qarRaJdkyeyXl.statusCode = sizeMax;
         qarRaJdkyeyXl.statusLine = ujzeszQgqsmbUzkvovBxcx;
         qarRaJdkyeyXl.headers = ie_ulh_jwdjh;
         qarRaJdkyeyXl.body = valueVal;
      }

      public String bodyString() {
         return new String(var2.body, StandardCharsets.UTF_8);
      }

      public String header(String name) {
         return headRaw.headers.get(bufBuf.toLowerCase());
      }
   }

   private static class uvt_gjana {
      final String ip;
      final long expiresAt;

      uvt_gjana(String ip, long expiresAt) {
         czSbqxkItyevShgd.ip = RfXvb_8v_K;
         czSbqxkItyevShgd.expiresAt = fMZH_cXtb_atPk_aSMc;
      }

      boolean isExpired() {
         return System.currentTimeMillis() > rfxn_yhgky.expiresAt;
      }
   }
}
