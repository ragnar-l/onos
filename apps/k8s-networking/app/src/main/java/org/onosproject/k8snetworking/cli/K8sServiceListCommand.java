/*
 * Copyright 2019-present Open Networking Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onosproject.k8snetworking.cli;

import com.google.common.collect.Lists;
import org.apache.karaf.shell.commands.Command;
import org.onosproject.cli.AbstractShellCommand;
import org.onosproject.k8snetworking.api.K8sServiceService;

import java.util.Comparator;
import java.util.List;

/**
 * Lists kubernetes services.
 */
@Command(scope = "onos", name = "k8s-services",
        description = "Lists all kubernetes services")
public class K8sServiceListCommand extends AbstractShellCommand {

    private static final String FORMAT = "%-50s%-30s%-30s";

    @Override
    protected void execute() {
        K8sServiceService service = get(K8sServiceService.class);
        List<io.fabric8.kubernetes.api.model.Service> services =
                Lists.newArrayList(service.services());
        services.sort(Comparator.comparing(s -> s.getMetadata().getName()));

        print(FORMAT, "Name", "Cluster IP", "Ports");

        for (io.fabric8.kubernetes.api.model.Service svc : services) {

            List<Integer> ports = Lists.newArrayList();

            svc.getSpec().getPorts().forEach(p -> ports.add(p.getPort()));

            print(FORMAT,
                    svc.getMetadata().getName(),
                    svc.getSpec().getClusterIP(),
                    ports.isEmpty() ? "" : ports);
        }
    }
}
