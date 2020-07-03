package br.com.codenation;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StatisticUtil {

    public static int average(int[] elements) {
        return (int) IntStream.of(elements).average().getAsDouble();
    }

    public static int mode(int[] elements) {

        Map<Integer, Long> repetidos = filtrarRepetidos(elements);
        long max = retornaValorMaximoRepeticao(repetidos);
        List<Integer> keys = retornaListaDeChavesComMaisRepeticoes(repetidos, max);

        return keys.size() == 1 ? keys.get(0) : 0;

    }

    private static List<Integer> retornaListaDeChavesComMaisRepeticoes(Map<Integer, Long> repetidos, long max) {
        return repetidos.entrySet()
                .stream().filter(item -> item.getValue() == max)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private static Long retornaValorMaximoRepeticao(Map<Integer, Long> repetidos) {
        return repetidos.values().stream().sorted(Comparator.reverseOrder()).findFirst().orElse(-1L);
    }

    private static Map<Integer, Long> filtrarRepetidos(int[] elements) {
        return Arrays.stream(elements)
                .boxed()
                .collect(Collectors.toList())
                .stream() // List<Integer> -> Stream
                .collect(Collectors.groupingBy(Function.identity()
                        , Collectors.counting()))
                .entrySet().stream()
                .filter(item -> item.getValue() > 1)
                .collect(Collectors.toMap(item -> item.getKey(), item -> item.getValue()));
    }

    public static int median(int[] elements) {
        if (elements.length % 2 == 0) {

            int mediana = elements.length / 2;
            List<Integer> ordered = Arrays.stream(elements)
                    .boxed()
                    .sorted().collect(Collectors.toList());

            int total = somarMedianaDeArraryPar(ordered, mediana);

            return total / 2;

        } else return (int) IntStream.of(elements).sorted().toArray()[elements.length / 2];
    }

    private static Integer somarMedianaDeArraryPar(List<Integer> ordered, int mediana) {
        return IntStream.range(0, ordered.size())
                .boxed()
                .collect(Collectors.toMap(index -> index, ordered::get))
                .entrySet().stream().filter(
                        i -> i.getKey() == mediana || i.getKey() == mediana - 1)
                .collect(Collectors.toMap(item -> item.getKey(), item -> item.getValue()))
                .values().stream().reduce(Integer::sum).get();
    }
}