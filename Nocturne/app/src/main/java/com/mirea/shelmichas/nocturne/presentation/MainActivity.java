package com.mirea.shelmichas.nocturne.presentation;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mirea.shelmichas.nocturne.R;

// Главный экран-хост: NavHostFragment и нижнее меню.
public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavHostFragment navHostFragment = (NavHostFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if (navHostFragment == null) {
            return;
        }
        navController = navHostFragment.getNavController();

        bottomNavigation = findViewById(R.id.bottomNavigation);
        // NavigationUI синхронизирует выбранный пункт с текущим экраном.
        NavigationUI.setupWithNavController(bottomNavigation, navController);
        // Свои правила переключения: корнем вкладок считается каталог.
        bottomNavigation.setOnItemSelectedListener(item -> navigateToTab(item.getItemId()));

        // Меню скрыто только на приветствии и на экране входа.
        navController.addOnDestinationChangedListener(
                (controller, destination, arguments) -> updateBottomNavigation(destination));
        updateBottomNavigation(navController.getCurrentDestination());
    }

    // Переключение вкладок: корнем считается каталог, состояние вкладок сохраняется.
    private boolean navigateToTab(int destinationId) {
        NavOptions.Builder builder = new NavOptions.Builder()
                .setLaunchSingleTop(true);
        if (destinationId == R.id.nav_catalog) {
            // Нажатие на «Каталог» всегда возвращает к списку каталога.
            builder.setPopUpTo(R.id.nav_catalog, true);
        } else {
            builder.setRestoreState(true)
                    .setPopUpTo(R.id.nav_catalog, false, true);
        }
        NavOptions options = builder.build();
        try {
            navController.navigate(destinationId, null, options);
        } catch (IllegalArgumentException e) {
            return false;
        }
        NavDestination current = navController.getCurrentDestination();
        return current != null && current.getId() == destinationId;
    }

    private void updateBottomNavigation(NavDestination destination) {
        if (destination == null) {
            return;
        }
        int id = destination.getId();
        if (id == R.id.nav_welcome || id == R.id.nav_login) {
            bottomNavigation.setVisibility(View.GONE);
            return;
        }
        bottomNavigation.setVisibility(View.VISIBLE);

        // На карточке места подсвечиваем вкладку, с которой её открыли.
        int tabId = id == R.id.nav_place_details ? previousDestinationId() : id;
        if (tabId == R.id.nav_catalog || tabId == R.id.nav_favorites || tabId == R.id.nav_profile) {
            bottomNavigation.getMenu().findItem(tabId).setChecked(true);
        }
    }

    private int previousDestinationId() {
        NavBackStackEntry previous = navController.getPreviousBackStackEntry();
        return previous == null ? 0 : previous.getDestination().getId();
    }
}
