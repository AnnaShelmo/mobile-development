package com.mirea.shelmichas.nocturne.presentation;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mirea.shelmichas.nocturne.R;

// Главный экран-хост: показывает фрагменты и нижнее меню.
public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_catalog) {
                openCatalog();
                return true;
            }
            if (id == R.id.nav_favorites) {
                openFavorites();
                return true;
            }
            if (id == R.id.nav_profile) {
                openProfile();
                return true;
            }
            return false;
        });

        // при возврате назад меню показываем или скрываем в зависимости от экрана
        getSupportFragmentManager().addOnBackStackChangedListener(this::syncChrome);

        if (savedInstanceState == null) {
            openWelcome();
        } else {
            syncChrome();
        }
    }

    // Стартовый экран.
    public void openWelcome() {
        setChrome(false);
        replaceRoot(new WelcomeFragment());
    }

    // Экран входа открывается поверх текущего экрана.
    public void openLogin() {
        setChrome(false);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new LoginFragment())
                .addToBackStack("login")
                .commit();
    }

    // Вкладки нижнего меню.
    public void openCatalog() {
        showTab(new CatalogFragment(), R.id.nav_catalog);
    }

    public void openFavorites() {
        showTab(new FavoritesFragment(), R.id.nav_favorites);
    }

    public void openProfile() {
        showTab(new ProfileFragment(), R.id.nav_profile);
    }

    // Страница места открывается с записью в бэк-стек.
    public void openPlaceDetails(int placeId) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, PlaceDetailsFragment.newInstance(placeId))
                .addToBackStack("place_details")
                .commit();
    }

    private void showTab(Fragment fragment, int menuId) {
        setChrome(true);
        replaceRoot(fragment);
        bottomNavigation.getMenu().findItem(menuId).setChecked(true);
    }

    // Вкладки не копятся в бэк-стеке: старый стек очищается.
    private void replaceRoot(Fragment fragment) {
        getSupportFragmentManager().popBackStackImmediate(null,
                FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    private void setChrome(boolean visible) {
        bottomNavigation.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    // Меню скрыто только на приветствии и на экране входа.
    private void syncChrome() {
        Fragment current = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if (current == null) {
            return;
        }
        boolean hideMenu = current instanceof WelcomeFragment || current instanceof LoginFragment;
        setChrome(!hideMenu);

        int menuId = 0;
        if (current instanceof CatalogFragment) {
            menuId = R.id.nav_catalog;
        } else if (current instanceof FavoritesFragment) {
            menuId = R.id.nav_favorites;
        } else if (current instanceof ProfileFragment) {
            menuId = R.id.nav_profile;
        }
        if (menuId != 0) {
            bottomNavigation.getMenu().findItem(menuId).setChecked(true);
        }
    }
}
