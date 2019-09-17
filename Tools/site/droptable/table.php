<?php
/* host, username, password, db_name */
$mysqli = new mysqli("localhost", "game", "Tn%N5H1mpD", "game");

/* check connection */
if ($mysqli->connect_errno) {
    printf("Connect failed: %s\n", $mysqli->connect_error);
    exit();
}

$items = array();

/* Preloading all item names to save time and resources later on */
$item_rows = $mysqli->query('SELECT item_id, item_name FROM item_definition');
while ($item_row = $item_rows->fetch_array()) {
  $items[$item_row['item_id']] = $item_row['item_name']; // items[item_id] = item_name
}
$item_rows->close();

/* Select queries return a resultset */
if ($npc_rows = $mysqli->query("SELECT * FROM npc_definition")) {
    printf("Found %d total npcs:<br /><br />", $npc_rows->num_rows);

    while ($npc_row = $npc_rows->fetch_array()) {
      $npc_id = $npc_row['npc_id'];
      $npc_name = $npc_row['name'];

      $query = "SELECT * FROM npc_drop WHERE npc_id = ".$npc_id;

      if ($drop_rows = $mysqli->query($query)) {
        if ($drop_rows->num_rows > 0) {
          echo '<br />Drops for '.$npc_name.'('.$npc_id.'):<br />';

          echo '<table>
          <tr>
              <th colspan="2">Item</th>
              <th>Quantity</th>
              <th>Rarity</th>
            </tr>
          ';
          while ($drop_row = $drop_rows->fetch_array()) {
            $item_id = $drop_row['item_id'];
            $min = $drop_row['minimum_count'];
            $max = $drop_row['maximum_count'];
            $chance = ucwords(strtolower(str_replace("_", " ", $drop_row['drop_frequency'])));
            $chance_style_tag = $drop_row['drop_frequency'];

            $item_name = $items[$item_id];
            $quantity = $min == $max ? "<td>".$min."</td>" : "<td>".$min." - ".$max."</td>";

            echo "
              <tr>
                <td><img src='http://gielinor.org/assets/img/itemsprites/".$item_id.".png'></td>
                <td>".$item_name."(".$item_id.")</td>
                ".$quantity."
                <td id='".$chance_style_tag."'>".$chance."</td>
              </tr>
              ";

          }
          echo '</table>';
        }
        $drop_rows->close();
      }
    }
    /* free result set */
    $npc_rows->close();
}

$mysqli->close();
?>
