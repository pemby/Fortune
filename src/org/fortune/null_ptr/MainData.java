package org.fortune.null_ptr;

import java.util.List;

public class MainData {
	public FortuneParserGen alist;
	public List<String> loginFortune;
	public List<String> knownPlayers;
	public boolean runPlugin;
	public boolean firstrun;

	public MainData(FortuneParserGen alist, List<String> loginFortune) {
		this.alist = alist;
		this.loginFortune = loginFortune;
	}
}