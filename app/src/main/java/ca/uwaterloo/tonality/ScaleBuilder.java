package ca.uwaterloo.tonality;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScaleBuilder {

    public static List<String> buildScale(String scale){
        List<String> scale_notes = new ArrayList<>();

        switch(scale){
            case "A Major":
                scale_notes = Arrays.asList("a4.wav", "b4.wav", "c#5.wav", "d5.wav", "e5.wav", "f#5.wav", "g#5.wav");
                break;
            case "A# Major":
                scale_notes = Arrays.asList("a#4.wav", "c5.wav", "d5.wav", "d#5.wav", "f5.wav", "g5.wav", "a5.wav");
                break;
            case "B Major":
                scale_notes = Arrays.asList("b4.wav", "c#5.wav", "d#5.wav", "e5.wav", "f#5.wav", "g#5.wav", "a#5.wav");
                break;
            case "C Major":
                scale_notes = Arrays.asList("c4.wav", "d4.wav", "e4.wav", "f4.wav", "g4.wav", "a4.wav", "b4.wav");
                break;
            case "C# Major":
                scale_notes = Arrays.asList("c#4.wav", "d#4.wav", "f4.wav", "f#4.wav", "g#4.wav", "a#4.wav", "c5.wav");
                break;
            case "D Major":
                scale_notes = Arrays.asList("d4.wav", "e4.wav", "f#4.wav", "g4.wav", "a4.wav", "b4.wav", "c#5.wav");
                break;
            case "D# Major":
                scale_notes = Arrays.asList("d#4.wav", "f4.wav", "g4.wav", "g#4.wav", "a#4.wav", "c5.wav", "d5.wav");
                break;
            case "E Major":
                scale_notes = Arrays.asList("e4.wav", "f#4.wav", "g#4.wav", "a4.wav", "b4.wav", "c#5.wav", "d#5.wav");
                break;
            case "F Major":
                scale_notes = Arrays.asList("f4.wav", "g4.wav", "a4.wav", "a#4.wav", "c5.wav", "d5.wav", "e5.wav");
                break;
            case "F# Major":
                scale_notes = Arrays.asList("f#4.wav", "g#4.wav", "a#4.wav", "b4.wav", "c#5.wav", "d#5.wav", "f5.wav");
                break;
            case "G Major":
                scale_notes = Arrays.asList("g4.wav", "a4.wav", "b4.wav", "c5.wav", "d5.wav", "e5.wav", "f#5.wav");
                break;
            case "G# Major":
                scale_notes = Arrays.asList("g#4.wav", "a#4.wav", "c5.wav", "c#5.wav", "d#5.wav", "f5.wav", "g5.wav");
                break;
            case "A Minor":
                scale_notes = Arrays.asList("a4.wav", "b4.wav", "c5.wav", "d5.wav", "e5.wav", "f5.wav", "g5.wav");
                break;
            case "A# Minor":
                scale_notes = Arrays.asList("a#4.wav", "c5.wav", "c#5.wav", "d#5.wav", "f5.wav", "f#5.wav", "g#5.wav");
                break;
            case "B Minor":
                scale_notes = Arrays.asList("b4.wav", "c#5.wav", "d5.wav", "e5.wav", "f#5.wav", "g5.wav", "a5.wav");
                break;
            case "C Minor":
                scale_notes = Arrays.asList("c4.wav", "d4.wav", "d#4.wav", "f4.wav", "g4.wav", "g#4.wav", "a#4.wav");
                break;
            case "C# Minor":
                scale_notes = Arrays.asList("c#4.wav", "d#4.wav", "e4.wav", "f#4.wav", "g#4.wav", "a4.wav", "b4.wav");
                break;
            case "D Minor":
                scale_notes = Arrays.asList("d4.wav", "e4.wav", "f4.wav", "g4.wav", "a4.wav", "a#4.wav", "c5.wav");
                break;
            case "D# Minor":
                scale_notes = Arrays.asList("d#4.wav", "f4.wav", "f#4.wav", "g#4.wav", "a#4.wav", "b4.wav", "c#5.wav");
                break;
            case "E Minor":
                scale_notes = Arrays.asList("e4.wav", "f#4.wav", "g4.wav", "a4.wav", "b4.wav", "c5.wav", "d5.wav");
                break;
            case "F Minor":
                scale_notes = Arrays.asList("f4.wav", "g4.wav", "g#4.wav", "a#4.wav", "c5.wav", "c#5.wav", "d#5.wav");
                break;
            case "F# Minor":
                scale_notes = Arrays.asList("f#4.wav", "g#4.wav", "a4.wav", "b4.wav", "c#5.wav", "d5.wav", "e5.wav");
                break;
            case "G Minor":
                scale_notes = Arrays.asList("g4.wav", "a4.wav", "a#4.wav", "c5.wav", "d5.wav", "d#5.wav", "f5.wav");
                break;
            case "G# Minor":
                scale_notes = Arrays.asList("g#4.wav", "a#4.wav", "b4.wav", "c#5.wav", "d#5.wav", "e5.wav", "f#5.wav");
                break;
        }

        return scale_notes;
    }

}
