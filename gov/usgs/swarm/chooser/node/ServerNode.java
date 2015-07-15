package gov.usgs.swarm.chooser.node;

import gov.usgs.swarm.Icons;
import gov.usgs.swarm.data.FileDataSource;
import gov.usgs.swarm.data.SeismicDataSource;

import javax.swing.Icon;

public class ServerNode extends AbstractChooserNode {
    private static final long serialVersionUID = 1L;
    private boolean broken;
    private SeismicDataSource source;

    public ServerNode(SeismicDataSource sds) {
        source = sds;
        label = source.getName();
    }

    public void setBroken(boolean b) {
        broken = b;
    }

    public Icon getIcon() {
        if (source instanceof FileDataSource) {
            return Icons.wave_folder;
        } else {
            if (!broken) {
                if (source.isStoreInUserConfig())
                    return Icons.server;
                else
                    return Icons.locked_server;
            } else {
                if (source.isStoreInUserConfig())
                    return Icons.broken_server;
                else
                    return Icons.broken_locked_server;
            }
        }
    }

    public SeismicDataSource getSource() {
        return source;
    }
}