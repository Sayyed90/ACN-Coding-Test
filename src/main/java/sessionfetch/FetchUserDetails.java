package sessionfetch;

import com.accenture.codingtest.springbootcodingtest.principle.UserPrinciple;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class FetchUserDetails {


    public FetchUserDetails() {

    }

    public String getNames(){
        UserPrinciple usr= (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name =usr.getUsername();
        return name;
    }

    public Set<String> getRoles(){
        UserPrinciple usr= (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String User=getNames();
        Collection<? extends GrantedAuthority> Ue=usr.getAuthorities();

        AtomicBoolean admin= new AtomicBoolean(false);
        AtomicBoolean productOwner= new AtomicBoolean(false);
        Ue.forEach(nm->{
                    if(nm.getAuthority().equalsIgnoreCase("ADMIN")){
                        admin.set(true);
                    }
                    if(nm.getAuthority().equalsIgnoreCase("PRODUCT_OWNER")){
                        productOwner.set(true);
                    }
                }
        );
        Set<String> getAllRoles=new HashSet<>();
        if(admin.get()) {
            getAllRoles.add(String.valueOf(admin.get()));
        }
        if(productOwner.get()) {
            getAllRoles.add(String.valueOf(productOwner.get()));
        }

        return getAllRoles;
    }


}
