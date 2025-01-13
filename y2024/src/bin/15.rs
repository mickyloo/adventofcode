use adv_code_2024::*;
use anyhow::*;
use code_timing_macros::time_snippet;
use const_format::concatcp;
use itertools::Itertools;
use std::fs::File;
use std::io::{BufRead, BufReader};
use std::ops::Index;

const DAY: &str = "15"; // TODO: Fill the day
const INPUT_FILE: &str = concatcp!("input/", DAY, ".txt");

const TEST: &str = "\
##########
#..O..O.O#
#......O.#
#.OO..O.O#
#..O@..O.#
#O#..O...#
#O..O..O.#
#.OO.O.OO#
#....O...#
##########

<vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
<<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
>^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
<><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^";

const TEST2: &str = "\
########
#..O.O.#
##@.O..#
#...O..#
#.#.O..#
#...O..#
#......#
########

<^^>>>vv<v>>v<<";

const TEST3: &str = "\
#######
#...#.#
#.....#
#..OO@#
#..O..#
#.....#
#######

<<<<<<<";

fn main() -> Result<()> {
    start_day(DAY);

    //region Part 1
    println!("=== Part 1 ===");

    fn print(grid: &Vec<Vec<char>>) {
        grid.into_iter().for_each(|row| {
            let foo = row.into_iter().join("");
            println!("{}", foo);
        })
    }

    fn push(grid: &mut Vec<Vec<char>>, cell: (usize, usize), dir: char) -> (usize, usize) {
        let delta: (i32, i32) = match dir {
            '<' => (0, -1),
            '^' => (-1, 0),
            '>' => (0, 1),
            'v' => (1, 0),
            _ => (0, 0),
        };
        let new_cell = (
            (cell.0 as i32 + delta.0) as usize,
            (cell.1 as i32 + delta.1) as usize,
        );
        let new_cell_val = grid[new_cell.0][new_cell.1];
        match new_cell_val {
            'O' | '[' | ']' => {
                push(grid, new_cell, dir);
                if grid[new_cell.0][new_cell.1] == '.' {
                    // still O
                    return push(grid, cell, dir);
                }
                cell
            }
            '.' => {
                // swap
                let curr_val = grid[cell.0][cell.1];
                grid[cell.0][cell.1] = '.';
                grid[new_cell.0][new_cell.1] = curr_val;
                (new_cell.0, new_cell.1)
            }
            _ => cell,
        }
    }

    fn part1<R: BufRead>(reader: R) -> Result<usize> {
        // TODO: Solve Part 1 of the puzzle
        let blocks = read_to_blocks(reader);
        let instructions = blocks[1].join("").chars().collect::<Vec<char>>();
        let mut grid = blocks[0]
            .iter()
            .map(|s| s.chars().collect_vec())
            .collect_vec();

        let mut curr = find(&grid, '@');

        for i in 0..instructions.len() {
            let cmd = instructions[i];
            curr = push(&mut grid, curr, cmd);
        }

        let mut answer = 0;
        for i in 0..grid.len() {
            for j in 0..grid.len() {
                if grid[i][j] == 'O' {
                    answer += 100 * i + j;
                }
            }
        }

        Ok(answer)
    }

    // TODO: Set the expected answer for the test input
    assert_eq!(2028, part1(BufReader::new(TEST2.as_bytes()))?);
    assert_eq!(10092, part1(BufReader::new(TEST.as_bytes()))?);

    let input_file = BufReader::new(File::open(INPUT_FILE)?);
    let result = time_snippet!(part1(input_file)?);
    println!("Result = {}", result);
    //endregion

    //region Part 2
    println!("\n=== Part 2 ===");

    fn push_heavy_vertical(
        grid: &mut Vec<Vec<char>>,
        cell: (usize, usize),
        dir: char,
    ) -> (usize, usize) {
        let delta: (i32, i32) = match dir {
            '^' => (-1, 0),
            'v' => (1, 0),
            _ => (0, 0),
        };
        let curr_val = grid[cell.0][cell.1];
        let new_cell = (
            (cell.0 as i32 + delta.0) as usize,
            (cell.1 as i32 + delta.1) as usize,
        );
        let new_cell_val = grid[new_cell.0][new_cell.1];
        cell
    }

    fn part2<R: BufRead>(reader: R) -> Result<usize> {
        let blocks = read_to_blocks(reader);
        let instructions = blocks[1].join("").chars().collect::<Vec<char>>();
        let mut grid: Vec<Vec<char>> = blocks[0]
            .iter()
            .map(|s| {
                let row = s
                    .chars()
                    .map(|c| match c {
                        '#' => vec!['#', '#'],
                        'O' => vec!['[', ']'],
                        '@' => vec!['@', '.'],
                        _ => vec!['.', '.'],
                    })
                    .collect_vec();

                row.into_iter().flatten().collect_vec()
            })
            .collect_vec();

        let mut curr = find(&grid, '@');

        for i in 0..instructions.len() {
            let cmd = instructions[i];
            curr = match cmd {
                '<' | '>' => push(&mut grid, curr, cmd),
                _ => push_heavy_vertical(&mut grid, curr, cmd),
            };
        }

        let mut answer = 0;
        for i in 0..grid.len() {
            for j in 0..grid.len() {
                if grid[i][j] == '[' {
                    answer += 100 * i + j;
                }
            }
        }

        print(&grid);

        Ok(0)
    }

    // assert_eq!(0, part2(BufReader::new(TEST.as_bytes()))?);
    assert_eq!(0, part2(BufReader::new(TEST3.as_bytes()))?);

    let input_file = BufReader::new(File::open(INPUT_FILE)?);
    let result = time_snippet!(part2(input_file)?);
    println!("Result = {}", result);
    //endregion

    Ok(())
}
