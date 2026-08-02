package com.mor.itas.planning.domain.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class SamplingService {
    public List<String> drawRandomSample(List<String> poolTins, int count) {
        if (poolTins == null || poolTins.isEmpty() || count <= 0) {
            return Collections.emptyList();
        }
        List<String> copy = new ArrayList<>(poolTins);
        Collections.shuffle(copy);
        return copy.subList(0, Math.min(count, copy.size()));
    }
}
