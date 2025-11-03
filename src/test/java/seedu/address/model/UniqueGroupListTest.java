package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.GroupId;

public class UniqueGroupListTest {
    @Test
    public void contains_nullGroup_throwsNullPointerException() {
        UniqueGroupList uniqueGroupList = new UniqueGroupList();
        assertThrows(NullPointerException.class, () -> uniqueGroupList.contains(null));
    }
    @Test
    public void contains_groupNotInList_returnsFalse() {
        UniqueGroupList uniqueGroupList = new UniqueGroupList();
        GroupId groupId = new GroupId("T01");
        assertFalse(uniqueGroupList.contains(groupId));
    }
    @Test
    public void contains_groupInList_returnsTrue() {
        UniqueGroupList uniqueGroupList = new UniqueGroupList();
        GroupId groupId = new GroupId("T01");
        uniqueGroupList.add(new Group(groupId));
        assertTrue(uniqueGroupList.contains(new GroupId("T01")));
    }
    @Test
    public void getGroupById_groupInList_returnsGroup() {
        UniqueGroupList uniqueGroupList = new UniqueGroupList();
        GroupId groupId = new GroupId("T01");
        Group expectedGroup = new Group(groupId);
        uniqueGroupList.add(expectedGroup);
        Group retrievedGroup = uniqueGroupList.getGroup(new GroupId("T01"));
        assertTrue(retrievedGroup.isSameGroup(expectedGroup.getGroupId()));
    }
    @Test
    public void getGroupById_groupNotInList_returnNull() {
        UniqueGroupList uniqueGroupList = new UniqueGroupList();
        assertEquals(null, uniqueGroupList.getGroup(new GroupId("T01")));
    }
    @Test
    public void setGroup_duplicateGroupId_throwsIllegalArgumentException() {
        UniqueGroupList uniqueGroupList = new UniqueGroupList();
        GroupId groupId = new GroupId("T01");
        Group group1 = new Group(groupId);
        Group group2 = new Group(groupId);
        uniqueGroupList.add(group1);
        assertThrows(IllegalArgumentException.class, () -> uniqueGroupList.setGroups(List.of(group1, group2)));
    }
}
