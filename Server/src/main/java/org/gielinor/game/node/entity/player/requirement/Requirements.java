package org.gielinor.game.node.entity.player.requirement;

import java.util.ArrayList;
import java.util.List;

import org.gielinor.game.node.entity.player.Player;


/**
 * @author David
 */
public class Requirements {

    private final List<Requirement> requirements = new ArrayList<>();

    public void addRequirement(Requirement requirement) {
        requirements.add(requirement);
    }

    public void addRequirements(Requirement... requirements) {
        for (Requirement requirement : requirements) {
            this.requirements.add(requirement);
        }
    }

    public void addRequirement(int index, Requirement requirement) {
        requirements.add(index, requirement);
    }

    public boolean hasRequirementsDisplayAll(Player player) {
        boolean hasRequirements = true;
        for (Requirement requirement : requirements) {
            if (!requirement.hasRequirement(player)) {
                requirement.displayErrorMessage(player);
                hasRequirements = false;
            }
        }
        return hasRequirements;
    }

    public boolean hasRequirementsDisplayOne(Player player) {
        for (Requirement requirement : requirements) {
            if (!requirement.hasRequirement(player)) {
                requirement.displayErrorMessage(player);
                return false;
            }
        }
        return true;
    }

    public boolean hasRequirements(Player player) {
        for (Requirement requirement : requirements) {
            if (!requirement.hasRequirement(player)) {
                return false;
            }
        }
        return true;
    }

    public void fulfillAll(Player player) {
        for (Requirement requirement : requirements) {
            requirement.fulfill(player);
        }
    }

}
