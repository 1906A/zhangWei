<template>
  <v-card>
    <v-flex xs12 sm10>
      <v-tree url="/item/category/list"
              :isEdit="isEdit"
              @handleAdd="handleAdd"
              @handleEdit="handleEdit"
              @handleDelete="handleDelete"
              @handleClick="handleClick"
      />
    </v-flex>
  </v-card>
</template>

<script>

  export default {
    name: "category",
    data() {
      return {
        isEdit: true,
      }
    },
    methods: {
      handleAdd(node) {
        console.log("add .... ");
        console.log(node);
        /*this.$http.post("/item/category/add", node)
          .then(value => {
            if (value.data == "succ") {
              alert("添加成功");
            } else {
              alert("添加失败");
            }
          }).catch(error => {
          alert("添加请求失败");
        });*/
      },
      handleEdit(node) {
        console.log("------------------------------");
        console.log(node);
        console.log("------------------------------");

        let id = node.id;
        alert(id);
        if (id == 0) {
          this.$http.post("/item/category/add", node)
            .then(value => {
              if (value.data == "succ") {
                alert("添加成功");
              } else {
                alert("添加失败");
              }
              window.location.reload();
            }).catch(error => {
            alert("添加请求失败");
          });
        } else {
          this.$http.post("/item/category/update", node)
            .then(value => {
              if (value.data == "succ") {
                alert("修改成功");
              } else if (value.data == "fall") {
                alert("修改失败");
              }
              window.location.reload();
            }).catch(error => {
            alert("修改请求失败")
          })

        }
      }
      ,
      handleDelete(id) {
        /*this.$http.get("/item/category/deleteById?id=" + id).then(value => {
          if (value.data == "succ") {
            alert("删除成功");
          } else if(value.data=="fall"){
            alert("删除失败");
          }
        }).catch(error=>{
          alert("删除请求失败");
        })*/

        this.$http.get("/item/category/deleteById",{params:{id:id}}).then(value => {
          if (value.data == "succ") {
            alert("删除成功");
          } else if(value.data=="fall"){
            alert("删除失败");
          }
        }).catch(error=>{
          alert("删除请求失败");
        })

      },
      handleClick(node) {
        console.log(node)
      }
    }
  };
</script>

<style scoped>

</style>
