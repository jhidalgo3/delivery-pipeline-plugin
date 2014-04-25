/*
This file is part of Delivery Pipeline Plugin.

Delivery Pipeline Plugin is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Delivery Pipeline Plugin is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Delivery Pipeline Plugin.
If not, see <http://www.gnu.org/licenses/>.
*/
package se.diabol.jenkins.pipeline.domain;

import au.com.centrumsystems.hudson.plugin.buildpipeline.trigger.BuildPipelineTrigger;
import hudson.model.FreeStyleProject;
import hudson.tasks.BuildTrigger;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.FailureBuilder;
import org.jvnet.hudson.test.JenkinsRule;
import se.diabol.jenkins.pipeline.DeliveryPipelineView;

import static org.junit.Assert.*;

public class ManualStepTest {

    @Rule
    public JenkinsRule jenkins = new JenkinsRule();

    @Test
    public void testIsManualTriggerAndResolveManualStep() throws Exception {
        FreeStyleProject upstream = jenkins.createFreeStyleProject("upstream");
        FreeStyleProject downstreamManual = jenkins.createFreeStyleProject("downstreamManual");
        upstream.getPublishersList().add(new BuildPipelineTrigger("downstreamManual", null));
        FreeStyleProject a = jenkins.createFreeStyleProject("a");
        FreeStyleProject b = jenkins.createFreeStyleProject("b");
        a.getPublishersList().add(new BuildTrigger("b", false));




        jenkins.getInstance().rebuildDependencyGraph();

        assertTrue(ManualStep.isManualTrigger(downstreamManual));
        assertFalse(ManualStep.isManualTrigger(upstream));


        assertFalse(ManualStep.isManualTrigger(a));
        assertFalse(ManualStep.isManualTrigger(b));

        assertNull(ManualStep.resolveManualStep(a));
        assertNull(ManualStep.resolveManualStep(b));
        assertNull(ManualStep.resolveManualStep(upstream));
        ManualStep step = ManualStep.resolveManualStep(downstreamManual);
        assertNotNull(step);
        assertNull(step.getUpstreamId());
        assertFalse(step.isEnabled());
        assertTrue(step.isPermission());
        assertNull(step.getPossibleVersions());
        assertEquals("downstreamManual", step.getUpstreamProject());


    }

    @Test
    public void testGetManualStepLatest() throws Exception {
        FreeStyleProject upstream = jenkins.createFreeStyleProject("upstream");
        FreeStyleProject downstream = jenkins.createFreeStyleProject("downstream");
        upstream.getPublishersList().add(new BuildPipelineTrigger("downstream", null));
        jenkins.getInstance().rebuildDependencyGraph();


        ManualStep step = ManualStep.getManualStepLatest(downstream, downstream.getLastBuild(), upstream.getLastBuild());
        assertNotNull(step);
        assertEquals("upstream", step.getUpstreamProject());
        assertNull(step.getUpstreamId());
        assertFalse(step.isEnabled());
        assertTrue(step.isPermission());
        assertNull(step.getPossibleVersions());


        jenkins.buildAndAssertSuccess(upstream);
        step = ManualStep.getManualStepLatest(downstream, downstream.getLastBuild(), upstream.getLastBuild());
        assertNotNull(step);
        assertEquals("upstream", step.getUpstreamProject());
        assertEquals("1", step.getUpstreamId());
        assertTrue(step.isEnabled());
        assertTrue(step.isPermission());
        assertNull(step.getPossibleVersions());

        downstream.getBuildersList().add(new FailureBuilder());
        DeliveryPipelineView view = new DeliveryPipelineView("hej", jenkins.getInstance());
        view.triggerManual("downstream", "upstream", "1");
        jenkins.waitUntilNoActivity();

        step = ManualStep.getManualStepLatest(downstream, downstream.getLastBuild(), upstream.getLastBuild());
        assertNotNull(step);
        assertEquals("upstream", step.getUpstreamProject());
        assertEquals("1", step.getUpstreamId());
        assertTrue(step.isEnabled());
        assertTrue(step.isPermission());
        assertNull(step.getPossibleVersions());


    }

    @Test
    public void testGetManualStepAggregated() throws Exception {
        FreeStyleProject upstream = jenkins.createFreeStyleProject("upstream");
        FreeStyleProject downstream = jenkins.createFreeStyleProject("downstream");
        upstream.getPublishersList().add(new BuildPipelineTrigger("downstream", null));
        jenkins.getInstance().rebuildDependencyGraph();
        jenkins.setQuietPeriod(0);

        ManualStep step = ManualStep.getManualStepAggregated(downstream, upstream);
        assertNotNull(step);
        assertEquals("upstream", step.getUpstreamProject());
        assertNull(step.getUpstreamId());
        assertFalse(step.isEnabled());
        assertTrue(step.isPermission());
        assertEquals(0, step.getPossibleVersions().size());


        jenkins.buildAndAssertSuccess(upstream);
        step = ManualStep.getManualStepAggregated(downstream, upstream);
        assertNotNull(step);
        assertEquals("upstream", step.getUpstreamProject());
        assertNull(step.getUpstreamId());
        assertTrue(step.isEnabled());
        assertTrue(step.isPermission());
        assertEquals(1, step.getPossibleVersions().size());

        downstream.getBuildersList().add(new FailureBuilder());
        DeliveryPipelineView view = new DeliveryPipelineView("hej", jenkins.getInstance());
        view.triggerManual("downstream", "upstream", "1");
        jenkins.waitUntilNoActivity();

        step = ManualStep.getManualStepAggregated(downstream, upstream);
        assertNotNull(step);
        assertEquals("upstream", step.getUpstreamProject());
        assertNull(step.getUpstreamId());
        assertTrue(step.isEnabled());
        assertTrue(step.isPermission());
        assertEquals(1, step.getPossibleVersions().size());

        jenkins.buildAndAssertSuccess(upstream);

        step = ManualStep.getManualStepAggregated(downstream, upstream);
        assertNotNull(step);
        assertEquals("upstream", step.getUpstreamProject());
        assertNull(step.getUpstreamId());
        assertTrue(step.isEnabled());
        assertTrue(step.isPermission());
        assertEquals(2, step.getPossibleVersions().size());

    }

}
