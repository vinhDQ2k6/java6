# Lab2 Cheatsheet — JPA mappings & DAOs

Quick reference for the `lab2` data model and repository usage.

## Tables (from `V1__j6security.sql`)
- users(username PK, password, enabled)
- roles(role_id PK, role_name)
- user_roles(username, roles) — composite PK (username, roles), foreign keys to users(username) and roles(role_id)

## Entities (what maps to what)
- `User`
  - `@Entity @Table(name = "users")`
  - `@Id String username` -> `username`
  - `String password`
  - `boolean enabled`
  - `@OneToMany(mappedBy = "user") List<UserRole> userRoles`

- `Role`
  - `@Entity @Table(name = "roles")`
  - `@Id @Column(name = "role_id") String id` -> `role_id`
  - `@Column(name = "role_name") String name` -> `role_name`
  - `@OneToMany(mappedBy = "role") List<UserRole> userRoles`

- `UserRoleKey` (composite key)
  - `@Embeddable` implements `Serializable`
  - fields: `username` (mapped to `username`), `roleId` (mapped to column `roles`)

- `UserRole` (join entity)
  - `@Entity @Table(name = "user_roles")`
  - `@EmbeddedId UserRoleKey id`
  - `@ManyToOne @MapsId("username") @JoinColumn(name = "username") User user`
  - `@ManyToOne @MapsId("roleId") @JoinColumn(name = "roles") Role role`

> Note: the `user_roles` foreign key column is named `roles` in the DB (not `role_id`).

## Repositories (DAO)
- `UserDAO extends JpaRepository<User, String>` — ID = `username`
- `RoleDAO extends JpaRepository<Role, String>` — ID = `role_id`
- `UserRoleDAO extends JpaRepository<UserRole, UserRoleKey>` — ID = composite `UserRoleKey`

## Common operations (examples)
- Create a user and role then assign a role:

```java
User user = new User();
user.setUsername("user@example.com");
user.setPassword("{noop}123");
user.setEnabled(true);
userDAO.save(user);

Role role = new Role();
role.setId("ROLE_USER");
role.setName("Nhân viên");
roleDAO.save(role);

UserRoleKey key = new UserRoleKey(user.getUsername(), role.getId());
UserRole ur = new UserRole();
ur.setId(key);
ur.setUser(user);
ur.setRole(role);
userRoleDAO.save(ur);
```

- Find user with roles:
```java
Optional<User> u = userDAO.findById("user@example.com");
// depending on fetch type, u.get().getUserRoles() may be populated
```

## Tips & pitfalls
- Column names must match the migration SQL exactly — we mapped `Role.id` -> `role_id` and `UserRoleKey.roleId` -> `roles`.
- For composite keys prefer `@Embeddable` + `@EmbeddedId` + `@MapsId` when parts of the key are relationships.
- Lombok `@Data` is convenient but can cause `toString()` or `equals()` recursion in bidirectional associations — use `@ToString.Exclude` or restrict equals/hashCode if needed.
- If you only need a simple many-to-many (no extra columns on the join table), using `@ManyToMany` with `@JoinTable` is simpler than an explicit join entity.

## Quick debugging commands
- Show generated SQL in Spring Boot (application.properties):
```
spring.jpa.show-sql=true
logging.level.org.hibernate.SQL=DEBUG
spring.jpa.properties.hibernate.format_sql=true
```
- Validate mappings at startup:
```
spring.jpa.hibernate.ddl-auto=validate
```

## Where to look in the repo
- Migration SQL: `lab2/src/main/resources/db/migration/V1__j6security.sql`
- Entities: `lab2/src/main/java/com/sof3062/web/model/` (`User.java`, `Role.java`, `UserRole.java`, `UserRoleKey.java`)
- Repositories: `lab2/src/main/java/com/sof3062/dao/` (`UserDAO`, `RoleDAO`, `UserRoleDAO`)

---
Small, focused reference — paste into your notes or open `lab2/CHEATSHEET.md` in the editor for quick lookup.
