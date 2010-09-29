package cz.cvut.promod.services.userService;

import cz.cvut.promod.services.Service;
import com.jgoodies.binding.value.ValueModel;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 17:43:19, 10.10.2009
 */

/**
 * UserService interface.
 * 
 * UserService holds the user identification.
 */
public interface UserService extends Service {

    /**
     * Returns the identifier of logged user.
     *
     * @return user's identifier
     */
    public String getUser();

    /**
     * Returns the observable value model representing the user's identifier.
     *
     * @return observable value model representing the user's identifier
     */
    public ValueModel getUserValueModel();

    /**
     * Changes the actual user.
     *
     * @param user is the identifier of the new user, an empty string stands for no user
     */
    public void setUser(final String user);
  
}
