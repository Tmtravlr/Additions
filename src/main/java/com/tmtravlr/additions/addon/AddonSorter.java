package com.tmtravlr.additions.addon;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModAPIManager;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.toposort.TopologicalSort;
import net.minecraftforge.fml.common.toposort.TopologicalSort.DirectedGraph;
import net.minecraftforge.fml.common.versioning.ArtifactVersion;

import com.google.common.collect.Maps;

/**
 * Sorts the addons based on dependencies. Most of the code adapted from ModSorter.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date July 2017
 */
public class AddonSorter {
    private DirectedGraph<Addon> addonGraph;

    private Addon beforeAll = new Addon();
    private Addon afterAll = new Addon();
    private Addon before = new Addon();
    private Addon after = new Addon();

    public AddonSorter() {
        buildGraph();
    }

    private void buildGraph() {
    	HashMap<String, Addon> nameLookup = Maps.newHashMap(AddonLoader.ADDONS_NAMED);
    	
        addonGraph = new DirectedGraph<Addon>();
        addonGraph.addNode(beforeAll);
        addonGraph.addNode(before);
        addonGraph.addNode(afterAll);
        addonGraph.addNode(after);
        addonGraph.addEdge(before, after);
        addonGraph.addEdge(beforeAll, before);
        addonGraph.addEdge(after, afterAll);

        for (Addon addon : AddonLoader.addonsLoaded) {
            addonGraph.addNode(addon);
        }

        for (Addon addon : AddonLoader.addonsLoaded) {
            boolean preDepAdded = false;
            boolean postDepAdded = false;

            for (String addonId : addon.dependencies) {
                preDepAdded = true;
                
                if (addonId.equals("*"))
                {
                    // We are "after" everything
                    addonGraph.addEdge(addon, afterAll);
                    addonGraph.addEdge(after, addon);
                    postDepAdded = true;
                }
                else
                {
                    addonGraph.addEdge(before, addon);
                    if (AddonLoader.ADDONS_NAMED.containsKey(addonId)) {
                        addonGraph.addEdge(AddonLoader.ADDONS_NAMED.get(addonId), addon);
                    }
                }
            }

            for (String addonId : addon.dependents)
            {
                postDepAdded = true;

                if (addonId.equals("*"))
                {
                    // We are "before" everything
                    addonGraph.addEdge(beforeAll, addon);
                    addonGraph.addEdge(addon, before);
                    preDepAdded = true;
                }
                else
                {
                    addonGraph.addEdge(addon, after);
                    if (AddonLoader.ADDONS_NAMED.containsKey(addonId)) {
                        addonGraph.addEdge(addon, AddonLoader.ADDONS_NAMED.get(addonId));
                    }
                }
            }

            if (!preDepAdded)
            {
                addonGraph.addEdge(before, addon);
            }

            if (!postDepAdded)
            {
                addonGraph.addEdge(addon, after);
            }
        }
    }

    public List<Addon> sort()
    {
        List<Addon> sortedList = TopologicalSort.topologicalSort(addonGraph);
        sortedList.removeAll(Arrays.asList(new Addon[] {beforeAll, before, after, afterAll}));
        return sortedList;
    }
}
