# Inventory App Roadmap

## Critical Next Steps

- Implement Login and Register screens in the Android app
- Integrate API calls for authentication (login/register)
- Store JWT token and user info locally after login
- Redirect to LoginActivity if not authenticated
- Add role-based menu access (e.g., admin-only menus)
- Add logout functionality

## Future Goals

- Implement user profile management (view/update profile)
- Add admin user management screens (list, edit, delete users)
- Implement data sync between app and server
- Add offline caching and sync mechanism
- Add password reset flow
- Add developer/admin menus (role-based)
- Polish UI/UX for authentication flows

## Notes
- All API endpoints require Authorization header with JWT after login
- Use OkHttp for networking (can switch to Retrofit later)
- Keep all data local if server is not accessible

