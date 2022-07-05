import java.util.ArrayList;

interface Lambda<T>{
	void Act(T t);
}

class Action{
	public ArrayList<Lambda<String>> action = new ArrayList<>();
	public void action(String state){
		actions.forEach(act -> {act.Act(state);});
	}
}

public class Dfa{
	public Boolean Start(String input, Action start, Action end, Action transition_a, Action transition_b, Action transition_c){
		String currentState = "q0";
		for(int i = 0; i < input.Length(); ++i){
			switch(currentState){
				case "q1":
					end.Action(currentState);
					if(element == a){
						transition_a.action(currentState);
						currentState = "q2";
					}
					if(element == b){
						transition_b.action(currentState);
						currentState = "q1";
					}
					if(element == c){
						transition_c.action(currentState);
						currentState = "q1";
					}
					start.action(currentState);
					break;
				case "q2":
					end.Action(currentState);
					if(element == a){
						transition_a.action(currentState);
						currentState = "q2";
					}
					if(element == b){
						transition_b.action(currentState);
						currentState = "q2";
					}
					if(element == c){
						transition_c.action(currentState);
						currentState = "q2";
					}
					start.action(currentState);
					break;
				case "q0":
					end.Action(currentState);
					if(element == a){
						transition_a.action(currentState);
						currentState = "q1";
					}
					if(element == b){
						transition_b.action(currentState);
						currentState = "q2";
					}
					if(element == c){
						transition_c.action(currentState);
						currentState = "q2";
					}
					start.action(currentState);
					break;
			}
		}
		return currentState.IsFinal();
	}
}