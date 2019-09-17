package org.gielinor.game.content.activity.minigame;

import org.gielinor.game.content.dialogue.DialogueBuilder;
import org.gielinor.game.content.dialogue.DialogueLayout;
import org.gielinor.game.node.entity.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * TODO: desperately needs a rewrite.
 */
public class Scoreboards {

	public static Scoreboard
			TOP_PKERS,
			TOP_KILLSTREAKS,
			TOP_ACHIEVER,
			TOP_TOTAL_EXP,
			TOP_HARDCORE_IRONMAN,
			TOP_AI_KDR,
			TOP_FFA_WIN_KDR;

	public static abstract class Scoreboard {

		Scoreboard(String title, String file) {
			this.title = title;
			//this.file = file;
		}

		public abstract void resort(ArrayList<ScoreboardEntry> entries);

		private String title;
		//private String file;
	}

	static class ScoreboardEntry {

		ScoreboardEntry(String player, String[] value) {
			this.player = player;
			this.value = value;
		}

		private String player;
		private String[] value;
	}

	public static void init() {
		TOP_PKERS = new Scoreboard("RuneWorld's Top Pkers", "top-pkers.txt") {
			@Override
			public void resort(ArrayList<ScoreboardEntry> entries) {
				Scoreboards.resort(entries, "double");
			}
		};

		TOP_KILLSTREAKS = new Scoreboard("RuneWorld's Top Killstreaks", "top-killstreaks.txt") {
			@Override
			public void resort(ArrayList<ScoreboardEntry> entries) {
				Scoreboards.resort(entries, "integer");
			}
		};

		TOP_ACHIEVER = new Scoreboard("RuneWorld's Top Achiever", "top-achievers.txt")  {
			@Override
			public void resort(ArrayList<ScoreboardEntry> entries) {
				Scoreboards.resort(entries, "integer");
			}
		};

		TOP_TOTAL_EXP = new Scoreboard("RuneWorld's Top Total Exp", "top-exp.txt")  {
			@Override
			public void resort(ArrayList<ScoreboardEntry> entries) {
				Scoreboards.resort(entries, "long");
			}
		};

		TOP_HARDCORE_IRONMAN = new Scoreboard("RuneWorld's Top Hardcore Ironman", "top-hc-iron.txt")  {
			@Override
			public void resort(ArrayList<ScoreboardEntry> entries) {
				Scoreboards.resort(entries, "long");
			}
		};

		TOP_AI_KDR = new Scoreboard("RuneWorld's Top AI KDR's", "top-artificial-kdr.txt") {
			@Override
			public void resort(ArrayList<ScoreboardEntry> entries) {
				Scoreboards.resort(entries, "double");
			}
		};

		TOP_FFA_WIN_KDR = new Scoreboard("RuneWorld's Top Champions", "top-ffa-win.txt") {
			@Override
			public void resort(ArrayList<ScoreboardEntry> entries) {
				Scoreboards.resort(entries, "double");
			}
		};
	}

	public static void startDialogue(Player player){
		new DialogueBuilder(DialogueLayout.OPTION)
				.firstOption("View PVP board", futurePlayer -> open(futurePlayer, TOP_PKERS))
				.secondOption("View kill-streak board", futurePlayer -> open(futurePlayer, TOP_KILLSTREAKS))
				.thirdOption("View achievements board", futurePlayer -> open(futurePlayer, TOP_ACHIEVER))
				.fourthOption("Next page", futurePlayer ->
						new DialogueBuilder( DialogueLayout.OPTION)
								.firstOption("View total-xp board",futurePlayer2 -> open(futurePlayer2, TOP_TOTAL_EXP))
								.secondOption(	"View hardcore iron-man board",futurePlayer2 -> open(futurePlayer2, TOP_HARDCORE_IRONMAN))
								.thirdOption("View AI kdr board", futurePlayer2 -> open(futurePlayer2, TOP_HARDCORE_IRONMAN))
								.fourthOption("Previous page", Scoreboards::startDialogue)
								.addCancel()
								.start(futurePlayer))
				.addCancel()
		.start(player);

	}



	public static ArrayList<ScoreboardEntry> getUpdatedList(Scoreboard scoreboard) {
		ArrayList<ScoreboardEntry> entries = new ArrayList<ScoreboardEntry>();
//		for(Player p : World.getPlayers()) {
//			if(p == null)
//				continue;
//			if(scoreboard == Scoreboards.TOP_PKERS) {
//				int kc = p.getPlayerKillingAttributes().getPlayerKills();
//				int dc = p.getPlayerKillingAttributes().getPlayerDeaths();
//				double kdr = (double) ((kc < 2 || dc < 2) ? (0) : (kc/dc));
//				entries.add(new ScoreboardEntry(p.getUsername(), new String[]{String.valueOf(kdr), String.valueOf(kc), String.valueOf(dc)}));
//			} else if(scoreboard == Scoreboards.TOP_KILLSTREAKS) {
//				entries.add(new ScoreboardEntry(p.getUsername(), new String[]{String.valueOf(p.getPlayerKillingAttributes().getPlayerKillStreak())}));
//			} else if(scoreboard == Scoreboards.TOP_TOTAL_EXP) {
//				entries.add(new ScoreboardEntry(p.getUsername(), new String[]{String.valueOf(p.getSkillManager().getTotalExp())}));
//			} else if(scoreboard == Scoreboards.TOP_ACHIEVER) {
//				entries.add(new ScoreboardEntry(p.getUsername(), new String[]{String.valueOf(p.getPointsHandler().getAchievementPoints())}));
//			} else if(scoreboard == Scoreboards.TOP_HARDCORE_IRONMAN && p.getGameMode().equals(GameMode.HARDCORE_IRONMAN)) {
//				entries.add(new ScoreboardEntry(p.getUsername(), new String[]{String.valueOf(p.getLives()), String.valueOf(p.getTotalPlayTime())}));
//			} else if(scoreboard.equals(TOP_AI_KDR)){
//				entries.add(new ScoreboardEntry(p.getUsername(), new String[]{String.valueOf(p.getPlayerKillingAttributes().getPlayerKillStreak())}));
//			} else if(scoreboard.equals(TOP_FFA_WIN_KDR))
//				entries.add(new ScoreboardEntry(p.getUsername(), new String[]
//						{
//								String.valueOf(p.getPlayerKillingAttributes().getFFAWinRatio()),
//								String.valueOf(p.getPlayerKillingAttributes().getFFAKDR())
//							}));
//		}
		scoreboard.resort(entries);
		return entries;
	}

	public static void open(Player player, Scoreboard scoreboard) {
		ArrayList<ScoreboardEntry> entries = getUpdatedList(scoreboard);
		int stringId = 6402;
//		for (int i = 0; i <= 50; stringId++, i++) {
//			if(i == 10) {
//				stringId = 8578;
//			}
//			ScoreboardEntry entry = i < entries.size() ? entries.get(i) : null;
//			String line = "";
//			DecimalFormat f = new DecimalFormat("##.00");
//			if(entry != null) {
//				if(scoreboard == Scoreboards.TOP_KILLSTREAKS) {
//					line = "@whi@Rank @or1@"+(i+1)+"@whi@ - "+entry.player.replaceAll("_", " ")+" - Killstreak: @or1@"+Integer.parseInt(entry.value[0]);
//				} else if(scoreboard == Scoreboards.TOP_PKERS) {
//					line = "@whi@Rank @or1@"+(i+1)+"@whi@ - "+entry.player.replaceAll("_", " ")+" - KDR: @or1@"+Double.valueOf(entry.value[0])+" @whi@Kills: @or1@"+Integer.parseInt(entry.value[1])+"@whi@ Deaths:@or1@ "+Integer.parseInt(entry.value[2])+"";
//				} else if(scoreboard == Scoreboards.TOP_TOTAL_EXP) {
//					line = "@whi@Rank @or1@"+(i+1)+"@whi@ - "+entry.player.replaceAll("_", " ")+" - Total Exp: @or1@"+ Misc.insertCommasToNumber(entry.value[0]);
//				} else if(scoreboard == Scoreboards.TOP_ACHIEVER) {
//					line = "@whi@Rank @or1@"+(i+1)+"@whi@ - "+entry.player.replaceAll("_", " ")+" - Achievement Points: @or1@"+Misc.insertCommasToNumber(entry.value[0]);
//				} else if(scoreboard == Scoreboards.TOP_HARDCORE_IRONMAN) {
//					line = "@whi@Rank @or1@"+(i+1)+"@whi@ - "+entry.player.replaceAll("_", " ")+" - Lives left: ("+getColorSyntax(Integer.parseInt(entry.value[0]))+entry.value[0]+"@whi@) - Time alive: @or1@"+Misc.getTimePlayed(Long.parseLong(entry.value[1]));
//				} else if(scoreboard.equals(TOP_AI_KDR)){
//					line = "@whi@Rank @or1@"+(i+1)+"@whi@ - "+entry.player.replaceAll("_", " ")+" - AI KDR: ("+Double.parseDouble(entry.value[0])+"@whi@)";
//				} else if(scoreboard.equals(TOP_FFA_WIN_KDR))
//					line = "@whi@Rank @or1@"+(i+1)+"@whi@ - "+entry.player.replaceAll("_", " ")+" - Win ratio: (@or1@"
//							+Math.round(Double.parseDouble(entry.value[0]) * 100)/100D+"@whi@)" +
//							" - KDR: (@or1@"
//							+Math.round(Double.parseDouble(entry.value[1]) * 100)/100D+"@whi@)"
//							;
//
//		}
//			player.getActionSender().sendString(stringId, line);
//		}
		entries.clear();
		player.getActionSender().sendInterface(6308);
		player.getActionSender().sendString(6400, "Scoreboard - "+scoreboard.title+"");
        player.getActionSender().sendString(6399, "");
        player.getActionSender().sendString(6401, "Close");
	}

	public static String getColorSyntax(int value){
		if(value == 1){
			return "@whi@";
		} else if(value == 2){
			return "@blu@";
		} else if(value == 3){
			return "@yel@";
		}else if(value >= 4){
			return "@red@";
		}
		return "";
	}

	private static void resort(ArrayList<ScoreboardEntry> entries, String type) {
		Collections.sort(entries, new Comparator<ScoreboardEntry>() {
			@Override
			public int compare(ScoreboardEntry player1, ScoreboardEntry player2) {
				if(type.equals("integer")) {
					int v1 = Integer.parseInt(player1.value[0]);
					int v2 = Integer.parseInt(player2.value[0]);
					if (v1 == v2) {
						return 0;
					} else if (v1 > v2) {
						return -1;
					} else {
						return 1;
					}
				} else if(type.equals("long")) {
					long v1 = Long.parseLong(player1.value[0]);
					long v2 = Long.parseLong(player2.value[0]);
					if (v1 == v2) {
						return 0;
					} else if (v1 > v2) {
						return -1;
					} else {
						return 1;
					}
				} else if(type.equals("double")) {
					double v1 = Double.parseDouble(player1.value[0]);
					double v2 = Double.parseDouble(player2.value[0]);
					if (v1 == v2) {
						return 0;
					} else if (v1 > v2) {
						return -1;
					} else {
						return 1;
					}
				} else {
					return 1;
				}
			}
		});
	}
}
