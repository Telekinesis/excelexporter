package excelexporter.ganglia.importing;

import org.telekinesis.commonclasses.io.LineParser;
import org.telekinesis.commonclasses.io.SshLineFileReader;

public class SshCsvLoader implements CsvLoader {
	private final String host;
	private final String username;
	private final String password;

	public SshCsvLoader(String host, String username, String password) {
		this.host = host;
		this.username = username;
		this.password = password;
	}

	@Override
	public void load(LineParser parser, String path) {
		SshLineFileReader reader = new SshLineFileReader(host, username, password);
		reader.read(path, parser, 1);
	}

}
