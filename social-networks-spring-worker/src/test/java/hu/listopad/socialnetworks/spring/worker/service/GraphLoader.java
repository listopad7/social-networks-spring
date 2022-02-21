package hu.listopad.socialnetworks.spring.worker.service;

import hu.listopad.socialnetworks.spring.data.WeightedGraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class GraphLoader {


    public static WeightedGraph loadGraph(String filename) throws IOException{


        File file = new File(filename);


        WeightedGraph g = new WeightedGraph();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                line.strip();
                String[] strings = line.split("\\s+");
                g.addVertex(Integer.parseInt(strings[0]));
                g.addVertex(Integer.parseInt(strings[1]));
                g.addEdge(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]));
            }
        }
        return g;
    }
}
