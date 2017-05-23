package com.example.startup.mapodp.app;

public class AppConfig {


	public static final String URL_LOGIN = "http://31.220.53.232/mapodp_v2/modul/admin/action.php?aksi=login";

	public static String getODPLocationURL() {
		return "http://31.220.53.232/mapodp_v2/modul/odp/action.php?aksi=odp";
	}


	public static String getDummy()
	{
		return "[{\"id_odp\":0,\"nama_odp\":\"odp0\",\"cluster_code\":\"odp0\",\"neqpt_reasue\":\"odp0\",\"tenoss_fracid\":\"odp0\",\"isiska_name\":\"odp0\",\"keterangan\":\"odp0\",\"alamat\":\"odp0\",\"lat\":-8.657817,\"long\":115.162731,\"kapasitas\":\"available\",\"id_admin\":1},{\"id_odp\":1,\"nama_odp\":\"odp1\",\"cluster_code\":\"odp1\",\"neqpt_reasue\":\"odp1\",\"tenoss_fracid\":\"odp1\",\"isiska_name\":\"odp1\",\"keterangan\":\"odp1\",\"alamat\":\"odp1\",\"lat\":-8.157817,\"long\":115.162731,\"kapasitas\":\"available\",\"id_admin\":1},{\"id_odp\":2,\"nama_odp\":\"odp2\",\"cluster_code\":\"odp2\",\"neqpt_reasue\":\"odp2\",\"tenoss_fracid\":\"odp2\",\"isiska_name\":\"odp2\",\"keterangan\":\"odp2\",\"alamat\":\"odp2\",\"lat\":-8.257817,\"long\":115.262731,\"kapasitas\":\"available\",\"id_admin\":1}]";
}
}
