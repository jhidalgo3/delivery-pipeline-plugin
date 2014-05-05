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
package se.diabol.jenkins.pipeline.trigger;

import au.com.centrumsystems.hudson.plugin.buildpipeline.BuildPipelineView;
import au.com.centrumsystems.hudson.plugin.buildpipeline.DownstreamProjectGridBuilder;
import hudson.model.ItemGroup;
import hudson.model.TopLevelItem;

public class BPPManualTrigger implements ManualTrigger {
    @Override
    public void triggerManual(String projectName, String upstreamName, String buildId, ItemGroup<? extends TopLevelItem> itemGroup) {
        BuildPipelineView view = new BuildPipelineView("", "", new DownstreamProjectGridBuilder(upstreamName), "1", false, "");
        view.triggerManualBuild(Integer.parseInt(buildId), projectName, upstreamName);
    }

}
