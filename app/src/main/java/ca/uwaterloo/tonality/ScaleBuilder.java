package ca.uwaterloo.tonality;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScaleBuilder {

    public static List<String> buildScale(String scale){
        List<String> scale_notes = new ArrayList<>();

        switch(scale){
            case "A Major":
                scale_notes = Arrays.asList("a4", "b4", "c#5", "d5", "e5", "f#5", "g#5");
                break;
            case "A# Major":
                scale_notes = Arrays.asList("a#4", "c5", "d5", "d#5", "f5", "g5", "a5");
                break;
            case "B Major":
                scale_notes = Arrays.asList("b4", "c#5", "d#5", "e5", "f#5", "g#5", "a#5");
                break;
            case "C Major":
                scale_notes = Arrays.asList("c4", "d4", "e4", "f4", "g4", "a4", "b4");
                break;
            case "C# Major":
                scale_notes = Arrays.asList("c#4", "d#4", "f4", "f#4", "g#4", "a#4", "c5");
                break;
            case "D Major":
                scale_notes = Arrays.asList("d4", "e4", "f#4", "g4", "a4", "b4", "c#5");
                break;
            case "D# Major":
                scale_notes = Arrays.asList("d#4", "f4", "g4", "g#4", "a#4", "c5", "d5");
                break;
            case "E Major":
                scale_notes = Arrays.asList("e4", "f#4", "g#4", "a4", "b4", "c#5", "d#5");
                break;
            case "F Major":
                scale_notes = Arrays.asList("f4", "g4", "a4", "a#4", "c5", "d5", "e5");
                break;
            case "F# Major":
                scale_notes = Arrays.asList("f#4", "g#4", "a#4", "b4", "c#5", "d#5", "f5");
                break;
            case "G Major":
                scale_notes = Arrays.asList("g4", "a4", "b4", "c5", "d5", "e5", "f#5");
                break;
            case "G# Major":
                scale_notes = Arrays.asList("g#4", "a#4", "c5", "c#5", "d#5", "f5", "g5");
                break;
            case "A Minor":
                scale_notes = Arrays.asList("a4", "b4", "c5", "d5", "e5", "f5", "g5");
                break;
            case "A# Minor":
                scale_notes = Arrays.asList("a#4", "c5", "c#5", "d#5", "f5", "f#5", "g#5");
                break;
            case "B Minor":
                scale_notes = Arrays.asList("b4", "c#5", "d5", "e5", "f#5", "g5", "a5");
                break;
            case "C Minor":
                scale_notes = Arrays.asList("c4", "d4", "d#4", "f4", "g4", "g#4", "a#4");
                break;
            case "C# Minor":
                scale_notes = Arrays.asList("c#4", "d#4", "e4", "f#4", "g#4", "a4", "b4");
                break;
            case "D Minor":
                scale_notes = Arrays.asList("d4", "e4", "f4", "g4", "a4", "a#4", "c5");
                break;
            case "D# Minor":
                scale_notes = Arrays.asList("d#4", "f4", "f#4", "g#4", "a#4", "b4", "c#5");
                break;
            case "E Minor":
                scale_notes = Arrays.asList("e4", "f#4", "g4", "a4", "b4", "c5", "d5");
                break;
            case "F Minor":
                scale_notes = Arrays.asList("f4", "g4", "g#4", "a#4", "c5", "c#5", "d#5");
                break;
            case "F# Minor":
                scale_notes = Arrays.asList("f#4", "g#4", "a4", "b4", "c#5", "d5", "e5");
                break;
            case "G Minor":
                scale_notes = Arrays.asList("g4", "a4", "a#4", "c5", "d5", "d#5", "f5");
                break;
            case "G# Minor":
                scale_notes = Arrays.asList("g#4", "a#4", "b4", "c#5", "d#5", "e5", "f#5");
                break;
        }

        return scale_notes;
    }

}
