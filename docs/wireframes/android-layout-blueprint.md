# Workout Log - Blueprint de Layouts Android

Este blueprint traduce el guion a pantallas concretas para implementar en XML Views con Fragments y Navigation Component.

## Paleta y estilo base

Colores recomendados para `res/values/colors.xml`:

```xml
<color name="primary">#1E3A5F</color>
<color name="primary_variant">#274C77</color>
<color name="secondary">#2ECC71</color>
<color name="background">#F5F7FA</color>
<color name="surface">#FFFFFF</color>
<color name="pain">#E57373</color>
<color name="text_primary">#17212B</color>
<color name="text_secondary">#667085</color>
<color name="stroke">#D7DDE5</color>
```

Dimensiones recomendadas para `res/values/dimens.xml`:

```xml
<dimen name="screen_padding">16dp</dimen>
<dimen name="card_padding">14dp</dimen>
<dimen name="card_radius">8dp</dimen>
<dimen name="item_spacing">10dp</dimen>
<dimen name="title_text_size">24sp</dimen>
<dimen name="subtitle_text_size">16sp</dimen>
<dimen name="body_text_size">14sp</dimen>
<dimen name="caption_text_size">12sp</dimen>
```

## `activity_main.xml`

Usa la estructura actual, pero cambia el FAB global por FABs dentro de los fragments que lo necesiten.

Jerarquia recomendada:

```text
CoordinatorLayout
  AppBarLayout
    MaterialToolbar @id/toolbar
  FragmentContainerView @id/nav_host_fragment_content_main
```

Notas:

- `MaterialToolbar` se configura desde `MainActivity`.
- El menu superior puede ser `res/menu/main_menu.xml`.
- El FAB global de la plantilla actual conviene quitarlo para no aparecer en pantallas donde no toca.

## `fragment_home.xml`

Objetivo: entrada de la app, resumen y accesos rapidos.

Jerarquia:

```text
NestedScrollView
  LinearLayout vertical @id/homeContainer
    MaterialCardView @id/cardHero
      TextView @id/textAppTitle
      TextView @id/textWelcome
    GridLayout @id/gridStats
      MaterialCardView @id/cardTotalWorkouts
      MaterialCardView @id/cardMonthWorkouts
      MaterialCardView @id/cardLastWeight
      MaterialCardView @id/cardLastPain
    MaterialCardView @id/cardLastWorkout
      TextView @id/textLastWorkoutTitle
      TextView @id/textLastWorkoutDate
      TextView @id/textLastWorkoutSummary
    MaterialButton @id/buttonAddWorkout
    MaterialButton @id/buttonOpenHistory
    MaterialButton @id/buttonOpenCalendar
```

Textos base:

- Titulo: `Workout Log`
- Bienvenida: `Registra tus entrenamientos diarios`
- Botones: `Añadir entrenamiento`, `Historial`, `Calendario`

Navegacion:

- `buttonAddWorkout` -> `AddEditWorkoutFragment`
- `buttonOpenHistory` -> `WorkoutListFragment`
- `buttonOpenCalendar` -> `CalendarFragment`

## `fragment_workout_list.xml`

Objetivo: historial con RecyclerView y acceso a crear.

Jerarquia:

```text
CoordinatorLayout
  LinearLayout vertical
    HorizontalScrollView
      ChipGroup @id/chipGroupFilters
        Chip @id/chipAll
        Chip @id/chipOnlyPain
        Chip @id/chipRecent
    TextView @id/textEmptyState
    RecyclerView @id/recyclerWorkouts
  FloatingActionButton @id/fabAddWorkout
```

Estados:

- Si hay datos: `recyclerWorkouts` visible, `textEmptyState` gone.
- Si no hay datos: `textEmptyState` visible con `No hay entrenamientos guardados`.

Click:

- Item de `recyclerWorkouts` -> `WorkoutDetailFragment` con argumento `workoutId`.
- `fabAddWorkout` -> `AddEditWorkoutFragment`.

## `item_workout.xml`

Objetivo: item compacto para el RecyclerView.

Jerarquia:

```text
MaterialCardView @id/cardWorkoutItem
  ConstraintLayout
    TextView @id/textWorkoutTraining
    TextView @id/textWorkoutDate
    TextView @id/textWorkoutWeight
    TextView @id/textWorkoutPainStatus
```

Contenido:

- `textWorkoutTraining`: `Pecho y triceps`
- `textWorkoutDate`: `13/05/2026`
- `textWorkoutWeight`: `Peso: 72.5 kg` o `Sin peso registrado`
- `textWorkoutPainStatus`: `Sin molestias` o `Molestia: muneca derecha`

Estilo:

- Card con radio `8dp`.
- Si `hasPain == true`, `textWorkoutPainStatus` usa fondo suave rojo.
- Si `hasPain == false`, usa fondo verde suave.

## `fragment_add_edit_workout.xml`

Objetivo: formulario reutilizable para crear y editar.

Jerarquia:

```text
NestedScrollView
  LinearLayout vertical @id/formContainer
    TextInputLayout @id/inputLayoutDate
      TextInputEditText @id/editTextDate
    TextInputLayout @id/inputLayoutTraining
      TextInputEditText @id/editTextTraining
    TextInputLayout @id/inputLayoutBodyWeight
      TextInputEditText @id/editTextBodyWeight
    MaterialSwitch @id/switchHasPain
    TextInputLayout @id/inputLayoutPainDescription
      TextInputEditText @id/editTextPainDescription
    TextInputLayout @id/inputLayoutNotes
      TextInputEditText @id/editTextNotes
    MaterialButton @id/buttonSaveWorkout
```

Comportamiento:

- Sin argumento `workoutId`: titulo `Añadir entrenamiento`.
- Con argumento `workoutId`: titulo `Editar entrenamiento` y campos precargados.
- `editTextDate` puede abrir DatePicker o rellenarse con la fecha actual.
- `inputLayoutPainDescription` visible solo si `switchHasPain` esta activo.

Validaciones:

- `editTextTraining` obligatorio.
- `editTextBodyWeight` debe parsear a `Double?`.
- Si `switchHasPain` esta desactivado, guarda `painDescription = null`.

## `fragment_workout_detail.xml`

Objetivo: lectura completa de un registro y acciones editar/eliminar.

Jerarquia:

```text
NestedScrollView
  LinearLayout vertical
    MaterialCardView @id/cardDetailHeader
      TextView @id/textDetailDate
      TextView @id/textDetailTraining
    LinearLayout vertical @id/detailInfoContainer
      TextView @id/textDetailWeight
      TextView @id/textDetailHasPain
      TextView @id/textDetailPainDescription
    MaterialCardView @id/cardNotes
      TextView @id/textDetailNotes
    LinearLayout horizontal
      MaterialButton @id/buttonEditWorkout
      MaterialButton @id/buttonDeleteWorkout
```

Comportamiento:

- Recibe `workoutId`.
- Carga el registro desde ViewModel/Room.
- `buttonEditWorkout` -> `AddEditWorkoutFragment` con el mismo `workoutId`.
- `buttonDeleteWorkout` -> muestra `MaterialAlertDialogBuilder`.

Dialogo de borrado:

```text
Titulo: Eliminar entrenamiento
Mensaje: ¿Seguro que quieres eliminar este entrenamiento?
Acciones: Cancelar / Eliminar
```

## `fragment_calendar.xml`

Objetivo: consultar entrenamientos por fecha.

Jerarquia:

```text
LinearLayout vertical
  CalendarView @id/calendarView
  TextView @id/textSelectedDate
  TextView @id/textCalendarEmptyState
  RecyclerView @id/recyclerCalendarWorkouts
```

Comportamiento:

- Al entrar, selecciona la fecha actual.
- `calendarView.setOnDateChangeListener` actualiza fecha seleccionada.
- Consulta `getWorkoutsByDate(date)`.
- Si no hay registros: muestra `No hay entrenamientos registrados para esta fecha`.

Formato de fecha:

- Usa un formato unico para guardar y consultar, por ejemplo `dd/MM/yyyy`.
- Evita mezclar `yyyy-MM-dd` y `dd/MM/yyyy` en Room.

## `fragment_about.xml`

Objetivo: pantalla simple desde el menu.

Jerarquia:

```text
NestedScrollView
  LinearLayout vertical
    TextView @id/textAboutLogo
    TextView @id/textAboutTitle
    TextView @id/textAboutVersion
    TextView @id/textAboutDescription
    TextView @id/textAboutAuthor
```

Contenido:

- App: `Workout Log`
- Version: `1.0`
- Descripcion: `Diario personal para registrar entrenamientos, peso corporal, molestias y notas por fecha.`
- Autor: tu nombre.

## `main_menu.xml`

Menu minimo:

```text
item @id/action_about -> About
item @id/action_filter_pain -> Ver solo molestias
```

Menu opcional si sobra tiempo:

```text
item @id/action_sort_date -> Ordenar por fecha
item @id/action_delete_all -> Eliminar todos
```

## Navigation Graph

Fragments:

```text
homeFragment
workoutListFragment
addEditWorkoutFragment
workoutDetailFragment
calendarFragment
aboutFragment
```

Acciones:

```text
homeFragment -> workoutListFragment
homeFragment -> addEditWorkoutFragment
homeFragment -> calendarFragment
homeFragment -> aboutFragment
workoutListFragment -> workoutDetailFragment
workoutListFragment -> addEditWorkoutFragment
workoutDetailFragment -> addEditWorkoutFragment
```

Argumentos:

```text
workoutDetailFragment:
  workoutId: integer

addEditWorkoutFragment:
  workoutId: integer, defaultValue: -1
```

## Orden de implementacion de layouts

1. `activity_main.xml` y `nav_graph.xml`.
2. `fragment_home.xml`.
3. `fragment_workout_list.xml` + `item_workout.xml`.
4. `fragment_add_edit_workout.xml`.
5. `fragment_workout_detail.xml`.
6. `fragment_calendar.xml`.
7. `fragment_about.xml`.
8. `main_menu.xml`.

