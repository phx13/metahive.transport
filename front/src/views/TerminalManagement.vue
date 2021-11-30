<template>
  <header-nav/>
  <management-table/>
</template>

<script>
import HeaderNav from "@/components/HeaderNav";
import ManagementTable from "@/components/ManagementTable";

export default {
  name: "TerminalManagement",
  components: {ManagementTable, HeaderNav}
}
</script>

<style scoped>

</style>