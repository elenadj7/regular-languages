import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UnitTests {
    
    @Test
    public void Test1(){
        RegularExpression regex = new RegularExpression("c.(a|b)");
        Dfa dfa = regex.TransformToDfa();
        Boolean expected = dfa.Accepts("ca");

        assertEquals(true, expected);
    }

    @Test
    public void Test2(){

        RegularExpression regex = new RegularExpression("a.b.c|d");
        Dfa dfa = regex.TransformToDfa();

        Boolean expected = dfa.Accepts("abc");

        assertEquals(true, expected);
    }

    @Test
    public void Test3(){

        RegularExpression regex = new RegularExpression("d.b");
        Dfa dfa = regex.TransformToDfa();

        int path = dfa.ShortestPath();

        assertEquals(2, path);
    }

    @Test
    public void Test4(){

        RegularExpression regex = new RegularExpression("a.(b|c)*");
        Dfa dfa = regex.TransformToDfa();

        int path = dfa.ShortestPath();

        assertEquals(1, path);
    }

    @Test
    public void Test5(){

        RegularExpression regex = new RegularExpression("a.b.(c|d)*.e");
        ENfa enfa = regex.TransformToENfa();

        Boolean expected = enfa.Accepts("abe");

        assertEquals(true, expected);
    }

    @Test
    public void Test6(){

        RegularExpression regex1 = new RegularExpression("a|b|c");
        RegularExpression regex2 = new RegularExpression("a.b.c");

        Boolean equals = regex1.TransformToDfa().AreEquals(regex2.TransformToDfa());

        assertEquals(false, equals);
    }

    @Test
    public void Test7(){

        ENfa automata = new ENfa();
        automata.SetStartState("q0");
        automata.AddFinalState("q5");
        automata.AddTransition("q0", 'b', "q0");
        automata.AddTransition("q0", 'a', "q1");
        automata.AddTransition("q1", 'b', "q1");
        automata.AddTransition("q1", '$', "q4");
        automata.AddTransition("q1", '$', "q2");
        automata.AddTransition("q2", 'a', "q2");
        automata.AddTransition("q2", 'b', "q5");
        automata.AddTransition("q3", 'a', "q4");
        automata.AddTransition("q4", 'b', "q3");
        automata.AddTransition("q4", 'a', "q5");

        ENfa kleen = automata.KleeneStar();

        Boolean accepts = kleen.Accepts("aaab");

        assertEquals(true, accepts);
    }

    @Test
    public void Test8(){

        ENfa automata = new ENfa();
        automata.SetStartState("q0");
        automata.AddFinalState("q5");
        automata.AddTransition("q0", 'b', "q0");
        automata.AddTransition("q0", 'a', "q1");
        automata.AddTransition("q1", 'b', "q1");
        automata.AddTransition("q1", '$', "q4");
        automata.AddTransition("q1", '$', "q2");
        automata.AddTransition("q2", 'a', "q2");
        automata.AddTransition("q2", 'b', "q5");
        automata.AddTransition("q3", 'a', "q4");
        automata.AddTransition("q4", 'b', "q3");
        automata.AddTransition("q4", 'a', "q5");

        Boolean accepts = automata.Accepts("aaaaabbb");

        assertEquals(false, accepts);
    }

    @Test
    public void Test9(){

        Dfa first = new Dfa();
        first.SetStartState("q0");
        first.AddTransition("q0", 'a', "q1");
        first.AddTransition("q0", 'b', "q0");
        first.AddTransition("q1", 'b', "q2");
        first.AddTransition("q1", 'a', "q1");
        first.AddTransition("q2", 'a', "q2");
        first.AddTransition("q2", 'b', "q2");
        first.AddFinalState("q2");
        first.AddFinalState("q1");

        
        Dfa second = new Dfa();
        second.SetStartState("p0");
        second.AddTransition("p0", 'a', "p0");
        second.AddTransition("p0", 'b', "p1");
        second.AddTransition("p1", 'a', "p2");
        second.AddTransition("p1", 'b', "p1");
        second.AddTransition("p2", 'a', "p2");
        second.AddTransition("p2", 'b', "p2");
        second.AddFinalState("p2");

        Dfa concatenation = first.Concatenation(second);

        Boolean accepts = concatenation.Accepts("abba");

        assertEquals(true, accepts);
    }

    @Test
    public void Test10(){

        Dfa first = new Dfa();
        first.SetStartState("q0");
        first.AddTransition("q0", 'a', "q1");
        first.AddTransition("q0", 'b', "q0");
        first.AddTransition("q1", 'b', "q2");
        first.AddTransition("q1", 'a', "q1");
        first.AddTransition("q2", 'a', "q2");
        first.AddTransition("q2", 'b', "q2");
        first.AddFinalState("q2");
        first.AddFinalState("q1");

        
        Dfa second = new Dfa();
        second.SetStartState("p0");
        second.AddTransition("p0", 'a', "p0");
        second.AddTransition("p0", 'b', "p1");
        second.AddTransition("p1", 'a', "p2");
        second.AddTransition("p1", 'b', "p1");
        second.AddTransition("p2", 'a', "p2");
        second.AddTransition("p2", 'b', "p2");
        second.AddFinalState("p2");

        Dfa union = first.Union(second);

        Boolean accepts = union.Accepts("ab");

        assertEquals(true, accepts);
    }

    @Test
    public void Test11(){

        ENfa automata = new ENfa();
        automata.SetStartState("q0");
        automata.AddFinalState("q5");
        automata.AddTransition("q0", 'b', "q0");
        automata.AddTransition("q0", 'a', "q1");
        automata.AddTransition("q1", 'b', "q1");
        automata.AddTransition("q1", '$', "q4");
        automata.AddTransition("q1", '$', "q2");
        automata.AddTransition("q2", 'a', "q2");
        automata.AddTransition("q2", 'b', "q5");
        automata.AddTransition("q3", 'a', "q4");
        automata.AddTransition("q4", 'b', "q3");
        automata.AddTransition("q4", 'a', "q5");

        Dfa dfa = automata.KleeneStar().TransformToDfa();

        Boolean accepts = dfa.Accepts("aaaab");

        assertEquals(true, accepts);
    }

    @Test 
    public void Test12(){

        Dfa first = new Dfa();
        first.SetStartState("e0");
        first.AddFinalState("e3");
        first.AddTransition("e0", 'a', "e1");
        first.AddTransition("e1", 'b', "e2");
        first.AddTransition("e2", 'a', "e0");
        first.AddTransition("e1", 'a', "e3");
        first.AddTransition("e3", 'a', "e0");
        first.AddTransition("e0", 'b', "e0");
        first.AddTransition("e2", 'b', "e1");
        first.AddTransition("e3", 'b', "e3");


        Dfa second = new Dfa();
        second.SetStartState("p0");
        second.AddFinalState("p3");
        second.AddTransition("p0", 'a', "p1");
        second.AddTransition("p1", 'b', "p2");
        second.AddTransition("p2", 'a', "p0");
        second.AddTransition("p1", 'a', "p3");
        second.AddTransition("p3", 'a', "p0");
        second.AddTransition("p0", 'b', "p0");
        second.AddTransition("p2", 'b', "p1");
        second.AddTransition("p3", 'b', "p3");

        Dfa intersection = first.Intersection(second);

        Boolean accepts = intersection.Accepts("aabbbbb");

        assertEquals(true, accepts);
    }

    @Test
    public void Test13(){

        ENfa first = new ENfa();
        first.SetStartState("p0");
        first.AddFinalState("p3");
        first.AddTransition("p0", '$', "p2");
        first.AddTransition("p2", 'a', "p3");
        first.AddTransition("p1", 'a', "p0");

        Dfa second = new Dfa();
        second.AddFinalState("q2");
        second.SetStartState("q0");
        second.AddTransition("q0", 'a', "q2");
        second.AddTransition("q2", 'a', "q1");
        second.AddTransition("q1", 'a', "q1");

        Dfa first2 = first.TransformToDfa();
        first2.RemoveInaccessibleStates();;

        Boolean equals = first2.AreEquals(second);

        assertEquals(true, equals);

    }

    @Test
    public void Test14(){

        RegularExpression regex1 = new RegularExpression("1.0.(1|0)*");
        RegularExpression regex2 = new RegularExpression("1.0.(1|0)*");

        Boolean equals = regex1.AreEquals(regex2);

        assertEquals(true, equals);
    }

}
