package cz.cvut.promod.services.userService;

import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.PresentationModel;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 17:51:26, 10.10.2009
 */

/**
 * UserService implementation
 */
public class UserServiceImpl extends Model implements UserService{

    public static String USER_PROPERTY = "user";    
    private String user;

    private final PresentationModel<UserServiceImpl> presentationModel = new PresentationModel<UserServiceImpl>(this);
    private final ValueModel userValueModel = presentationModel.getModel(USER_PROPERTY);

    /** {@inheritDoc} */
    public String getUser() {
        return user;
    }

    /** {@inheritDoc} */
    public void setUser(final String user) {
        final String oldUser = this.user;
        this.user = user;
        firePropertyChange(USER_PROPERTY, oldUser, user);
    }

    /** {@inheritDoc} */
    public ValueModel getUserValueModel() {
        return userValueModel;
    }

    /** {@inheritDoc} */
    public boolean check() {            
        return true;
    }

}
