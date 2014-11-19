import java.util.ArrayList;

public class BaseballElimination {

    private class team {

        private String name;
        private int wins;
        private int losses;
        private int remaining;
        private int[] games;

        protected team(String name, int wins, int losses, int remaining, int[] games) {

            this.name = name;
            this.wins = wins;
            this.losses = losses;
            this.remaining = remaining;
            this.games = games;
        }
    }

    private int N;
    private ArrayList<team> teams;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {

        // Read file
        In teamsIn = new In(filename);

        this.N = teamsIn.readInt();
        this.teams = new ArrayList<team>();

        String teamName;
        int wins, losses, remaining, games[];

        for (int a = 0; a < this.N; a++) {

            teamName = teamsIn.readString();
            wins = teamsIn.readInt();
            losses = teamsIn.readInt();
            remaining = teamsIn.readInt();

            games = new int[this.N];
            for (int b = 0; b < this.N; b++) {
                games[b] = teamsIn.readInt();
            }

            teams.add(new team(teamName, wins, losses, remaining, games));
        }
    }

    // number of teams
    public int numberOfTeams() {

        return this.N;
    }

    // all teams
    public Iterable<String> teams() {

        ArrayList<String> names = new ArrayList<String>();

        for (team a : this.teams) {
            names.add(a.name);
        }

        return names;
    }

    // number of wins for given team
    public int wins(String team) {

        return teams.get(this.findTeamIndex(team)).wins;
    }

    // number of losses for given team
    public int losses(String team) {

        return teams.get(this.findTeamIndex(team)).losses;
    }

    // number of remaining games for given team
    public int remaining(String team){

        return teams.get(this.findTeamIndex(team)).remaining;
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {

        return teams.get(this.findTeamIndex(team1)).games[this.findTeamIndex(team2)];
    }

//    // is given team eliminated?
//    public boolean isEliminated(String team) {
//
//    }

//    // subset R of teams that eliminates given team; null if not eliminated
//    public Iterable<String> certificateOfElimination(String team) {
//
//    }

    private int findTeamIndex(String name) {

        for (int a = 0; a < this.teams.size(); a++) {
            if (teams.get(a).name.compareTo(name) == 0)
                    return a;
        }

        throw new IllegalArgumentException();
    }

    public static void main(String[] args) {

        BaseballElimination baseballElimination = new BaseballElimination(args[0]);

        StdOut.println(baseballElimination.teams());
        StdOut.println(baseballElimination.remaining("New_York"));
    }

}



