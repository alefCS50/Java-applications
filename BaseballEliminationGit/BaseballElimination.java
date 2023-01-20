import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;

public class BaseballElimination {
    private FlowNetwork G;

    private int[] wins;
    private int[] loss;
    private int[] left;
    private int[][] games;
    private SET<String> teams;
    private HashMap<String, Integer> TeamToIndex;
    private HashMap<String, Bag<String>> eliTeams = new HashMap<>();
    private final int teamNum;


    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        if (filename == null) throw new IllegalArgumentException("");


        In in = new In(filename);
        teamNum = in.readInt();
        wins = new int[teamNum];
        loss = new int[teamNum];
        left = new int[teamNum];
        games = new int[teamNum][teamNum];
        TeamToIndex = new HashMap<>();
        teams = new SET<>();

        for (int row = 0; row < teamNum; row++) {
            String team = in.readString();
            TeamToIndex.put(team, row);
            teams.add(team);
            wins[row] = in.readInt();
            loss[row] = in.readInt();
            left[row] = in.readInt();

            for (int i = 0; i < teamNum; i++) {
                games[row][i] = in.readInt();
            }
        }

    }


    // number of teams
    public int numberOfTeams() {
        return teamNum;
    }

    // all teams
    public Iterable<String> teams() {
        return teams;
    }

    // number of wins for given team
    public int wins(String team) {
        if (!teams.contains(team)) throw new IllegalArgumentException("");
        int index = TeamToIndex.get(team);
        return wins[index];
    }

    // number of losses for given team
    public int losses(String team) {
        if (!teams.contains(team)) throw new IllegalArgumentException("");
        int index = TeamToIndex.get(team);
        return loss[index];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        if (!teams.contains(team)) throw new IllegalArgumentException("");
        int index = TeamToIndex.get(team);
        return left[index];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (!teams.contains(team1) || !teams.contains(team2)) throw new IllegalArgumentException("");

        int index1 = TeamToIndex.get(team1);
        int index2 = TeamToIndex.get(team2);

        return games[index1][index2];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        if (!teams.contains(team)) throw new IllegalArgumentException("");


        int teamIndex = TeamToIndex.get(team);

        //check trivially eliminated
        int mostWins = 0;
        for (int i = 0; i < wins.length; i++) {
            if (wins[i] > mostWins && i != teamIndex) mostWins = wins[i];
        }
        if (mostWins > wins[teamIndex] + left[teamIndex]) {
            Bag<String> temp = new Bag<>();
            //add teams into bag
            for (String s : teams()) {
                if (wins[TeamToIndex.get(s)] > wins[teamIndex] + left[teamIndex]) temp.add(s);
            }
            eliTeams.put(team, temp);
            return true;
        }


        //check nontrivial elimination
        FlowNetwork G = createNet(team);
        FordFulkerson f = new FordFulkerson(G, 0, G.V() - 1);

        for (FlowEdge e : G.adj(0)) {
            if (e.flow() != e.capacity()) {
                addCertificate(G, f, team);
                return true;
            }
        }

        eliTeams.remove(team);
        return false;

    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (!teams.contains(team)) throw new IllegalArgumentException("");
        if (!eliTeams.containsKey(team)) return null;
        if (eliTeams.get(team) == null) {
            isEliminated(team);
            return certificateOfElimination(team);
        } else return eliTeams.get(team);


    }

    private static int getFact(int n) {
        int f = 1;

        for (int i = n; i >= 1; i--) {
            f *= i;
        }

        return f;
    }

    private FlowNetwork createNet(String team) {
        int numGames = numberOfTeams() * (numberOfTeams() - 1) / 2;
        int numVertices = numberOfTeams() + numGames + 2;


        FlowNetwork G = new FlowNetwork(numVertices);

        //s is vertex number 0
        //t is last vertex
        int s = 0;
        int t = numVertices - 1;
        int teamIndex = TeamToIndex.get(team);

        int gameCount = 1;
        //connecting s to every game
        for (int i = 0; i < numberOfTeams(); i++) {
            if (i == teamIndex) continue;
            for (int j = i + 1; j < numberOfTeams(); j++) {
                if (j == teamIndex) continue;

                //add edges from source to games
                G.addEdge(new FlowEdge(0, gameCount, games[i][j]));

                //add edges from games to teams
                G.addEdge(new FlowEdge(gameCount, numGames + i + 1, Double.POSITIVE_INFINITY));
                G.addEdge(new FlowEdge(gameCount, numGames + j + 1, Double.POSITIVE_INFINITY));
                gameCount++;
                //}
            }
            int teamCount = numGames + 1 + i;
            G.addEdge(new FlowEdge(teamCount, t, Math.max(0, wins[teamIndex] + left[teamIndex] - wins[i])));
        }


        return G;


    }


    private void addCertificate(FlowNetwork G, FordFulkerson f, String team) {
        Bag<String> bag = new Bag<>();
        int teamCount = (numberOfTeams() * (numberOfTeams() - 1) / 2) + 1;


        for (String current : teams()) {
            int teamIndex = TeamToIndex.get(current);
            if (f.inCut(teamCount + teamIndex)) {
                bag.add(current);
            }
        }
        eliTeams.put(team, bag);
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }


}
