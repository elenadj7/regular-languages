import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UnitTests {
    
    @Test
    public void Test1(){
        RegularExpression regex = new RegularExpression("c.(a|b)");
        Dfa dfa = regex.transformToDfa();
        Boolean expected = dfa.accepts("ca");

        assertEquals(true, expected);
    }

    @Test
    public void Test2(){

        RegularExpression regex = new RegularExpression("a.b.c|d");
        Dfa dfa = regex.transformToDfa();

        Boolean expected = dfa.accepts("abc");

        assertEquals(true, expected);
    }

    @Test
    public void Test3(){

        RegularExpression regex = new RegularExpression("d.b");
        Dfa dfa = regex.transformToDfa();

        int path = dfa.shortestPath();

        assertEquals(2, path);
    }

    @Test
    public void Test4(){

        RegularExpression regex = new RegularExpression("a.(b|c)*");
        Dfa dfa = regex.transformToDfa();

        int path = dfa.shortestPath();

        assertEquals(1, path);
    }

    @Test
    public void Test5(){

        RegularExpression regex = new RegularExpression("a.b.(c|d)*.e");
        ENfa enfa = regex.transformToENfa();

        Boolean expected = enfa.accepts("abe");

        assertEquals(true, expected);
    }

    @Test
    public void Test6(){

        RegularExpression regex1 = new RegularExpression("a|b|c");
        RegularExpression regex2 = new RegularExpression("a.b.c");

        Boolean equals = regex1.transformToDfa().areEquals(regex2.transformToDfa());

        assertEquals(false, equals);
    }

    @Test
    public void Test7(){

        ENfa automata = new ENfa();
        automata.setStartState("q0");
        automata.addFinalState("q5");
        automata.addTransition("q0", 'b', "q0");
        automata.addTransition("q0", 'a', "q1");
        automata.addTransition("q1", 'b', "q1");
        automata.addTransition("q1", '$', "q4");
        automata.addTransition("q1", '$', "q2");
        automata.addTransition("q2", 'a', "q2");
        automata.addTransition("q2", 'b', "q5");
        automata.addTransition("q3", 'a', "q4");
        automata.addTransition("q4", 'b', "q3");
        automata.addTransition("q4", 'a', "q5");

        ENfa kleen = automata.kleeneStar();

        Boolean accepts = kleen.accepts("aaab");

        assertEquals(true, accepts);
    }

    @Test
    public void Test8(){

        ENfa automata = new ENfa();
        automata.setStartState("q0");
        automata.addFinalState("q5");
        automata.addTransition("q0", 'b', "q0");
        automata.addTransition("q0", 'a', "q1");
        automata.addTransition("q1", 'b', "q1");
        automata.addTransition("q1", '$', "q4");
        automata.addTransition("q1", '$', "q2");
        automata.addTransition("q2", 'a', "q2");
        automata.addTransition("q2", 'b', "q5");
        automata.addTransition("q3", 'a', "q4");
        automata.addTransition("q4", 'b', "q3");
        automata.addTransition("q4", 'a', "q5");

        Boolean accepts = automata.accepts("aaaaabbb");

        assertEquals(false, accepts);
    }

    @Test
    public void Test9(){

        Dfa first = new Dfa();
        first.setStartState("q0");
        first.addTransition("q0", 'a', "q1");
        first.addTransition("q0", 'b', "q0");
        first.addTransition("q1", 'b', "q2");
        first.addTransition("q1", 'a', "q1");
        first.addTransition("q2", 'a', "q2");
        first.addTransition("q2", 'b', "q2");
        first.addFinalState("q2");
        first.addFinalState("q1");

        
        Dfa second = new Dfa();
        second.setStartState("p0");
        second.addTransition("p0", 'a', "p0");
        second.addTransition("p0", 'b', "p1");
        second.addTransition("p1", 'a', "p2");
        second.addTransition("p1", 'b', "p1");
        second.addTransition("p2", 'a', "p2");
        second.addTransition("p2", 'b', "p2");
        second.addFinalState("p2");

        Dfa concatenation = first.concatenation(second);

        Boolean accepts = concatenation.accepts("abba");

        assertEquals(true, accepts);
    }

    @Test
    public void Test10(){

        Dfa first = new Dfa();
        first.setStartState("q0");
        first.addTransition("q0", 'a', "q1");
        first.addTransition("q0", 'b', "q0");
        first.addTransition("q1", 'b', "q2");
        first.addTransition("q1", 'a', "q1");
        first.addTransition("q2", 'a', "q2");
        first.addTransition("q2", 'b', "q2");
        first.addFinalState("q2");
        first.addFinalState("q1");

        
        Dfa second = new Dfa();
        second.setStartState("p0");
        second.addTransition("p0", 'a', "p0");
        second.addTransition("p0", 'b', "p1");
        second.addTransition("p1", 'a', "p2");
        second.addTransition("p1", 'b', "p1");
        second.addTransition("p2", 'a', "p2");
        second.addTransition("p2", 'b', "p2");
        second.addFinalState("p2");

        Dfa union = first.union(second);

        Boolean accepts = union.accepts("ab");

        assertEquals(true, accepts);
    }

    @Test
    public void Test11(){

        ENfa automata = new ENfa();
        automata.setStartState("q0");
        automata.addFinalState("q5");
        automata.addTransition("q0", 'b', "q0");
        automata.addTransition("q0", 'a', "q1");
        automata.addTransition("q1", 'b', "q1");
        automata.addTransition("q1", '$', "q4");
        automata.addTransition("q1", '$', "q2");
        automata.addTransition("q2", 'a', "q2");
        automata.addTransition("q2", 'b', "q5");
        automata.addTransition("q3", 'a', "q4");
        automata.addTransition("q4", 'b', "q3");
        automata.addTransition("q4", 'a', "q5");

        Dfa dfa = automata.kleeneStar().transformToDfa();

        Boolean accepts = dfa.accepts("aaaab");

        assertEquals(true, accepts);
    }

    @Test 
    public void Test12(){

        Dfa first = new Dfa();
        first.setStartState("e0");
        first.addFinalState("e3");
        first.addTransition("e0", 'a', "e1");
        first.addTransition("e1", 'b', "e2");
        first.addTransition("e2", 'a', "e0");
        first.addTransition("e1", 'a', "e3");
        first.addTransition("e3", 'a', "e0");
        first.addTransition("e0", 'b', "e0");
        first.addTransition("e2", 'b', "e1");
        first.addTransition("e3", 'b', "e3");


        Dfa second = new Dfa();
        second.setStartState("p0");
        second.addFinalState("p3");
        second.addTransition("p0", 'a', "p1");
        second.addTransition("p1", 'b', "p2");
        second.addTransition("p2", 'a', "p0");
        second.addTransition("p1", 'a', "p3");
        second.addTransition("p3", 'a', "p0");
        second.addTransition("p0", 'b', "p0");
        second.addTransition("p2", 'b', "p1");
        second.addTransition("p3", 'b', "p3");

        Dfa intersection = first.intersection(second);

        Boolean accepts = intersection.accepts("aabbbbb");

        assertEquals(true, accepts);
    }

    @Test
    public void Test13(){

        ENfa first = new ENfa();
        first.setStartState("p0");
        first.addFinalState("p3");
        first.addTransition("p0", '$', "p2");
        first.addTransition("p2", 'a', "p3");
        first.addTransition("p1", 'a', "p0");

        Dfa second = new Dfa();
        second.addFinalState("q2");
        second.setStartState("q0");
        second.addTransition("q0", 'a', "q2");
        second.addTransition("q2", 'a', "q1");
        second.addTransition("q1", 'a', "q1");

        Dfa first2 = first.transformToDfa();
        first2.removeInaccessibleStates();;

        Boolean equals = first2.areEquals(second);

        assertEquals(true, equals);

    }

    @Test
    public void Test14(){

        RegularExpression regex1 = new RegularExpression("1.0.(1|0)*");
        RegularExpression regex2 = new RegularExpression("1.0.(1|0)*");

        Boolean equals = regex1.areEquals(regex2);

        assertEquals(true, equals);
    }

}
