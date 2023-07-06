public @interface Zamena {
      /*  private Epic checkEpicStatus(int id) { //честно не знаю как подругому сделать, так работает
        Epic epic = epicStorage.get(id);
        List<SubTask> list = epic.getSubtaskIds();
        if (epic.getStatus() == "NEW") {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getStatus() != "NEW") {
                    epic.setStatus("IN_PROGRESS");
                    updateEpic(epic);
                }
            }
        } else if (epic.getStatus() == "IN_PROGRESS") {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getStatus() != "DONE") {
                    epic.setStatus("IN_PROGRESS");
                    updateEpic(epic);
                    return epic;
                }
                epic.setStatus("DONE");
                updateEpic(epic);
            }
        }
        return epic;
    }*/


}
